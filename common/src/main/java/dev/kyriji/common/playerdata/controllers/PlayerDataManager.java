package dev.kyriji.common.playerdata.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.documents.CoreConfig;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.common.config.models.MongoConnection;
import dev.kyriji.common.database.controllers.DatabaseManager;
import dev.kyriji.common.database.enums.DatabaseType;
import dev.kyriji.common.database.records.DatabaseConnection;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;
import dev.kyriji.common.playerdata.models.PlayerDataDocument;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class PlayerDataManager {

	private static final int MAX_RETRY_ATTEMPTS = 60;
	private static final long RETRY_DELAY_MS = 1000;
	private static final long STARTUP_DELAY_MS = 1000;

	private static final Logger logger = Logger.getLogger(PlayerDataManager.class.getName());

	private LuckPerms luckPerms;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private final TritonPlayerDataHook hook;

	public PlayerDataManager(TritonPlayerDataHook hook) {
		this.hook = hook;

		CoreConfig config = ConfigManager.getConfig(ConfigType.CORE);
		if(config == null) throw new NullPointerException("Core config not found");

		MongoConnection playerDataConnection = config.getPlayerDataConnection();
		DatabaseManager.addDatabase(DatabaseType.PLAYER_DATA, playerDataConnection.getUri(), playerDataConnection.getDatabase());

		initializeLuckPerms();
		registerEventHooks();
	}

	private void initializeLuckPerms() {
		int[] attempts = {0};

		scheduler.scheduleAtFixedRate(() -> {
			try {
				luckPerms = LuckPermsProvider.get();
				logger.info("Successfully connected to LuckPerms!");
				scheduler.shutdown();
			} catch (IllegalStateException e) {
				attempts[0]++;

				if (attempts[0] >= MAX_RETRY_ATTEMPTS) {
					logger.severe("Failed to connect to LuckPerms after " + MAX_RETRY_ATTEMPTS + " attempts!");
					scheduler.shutdown();
					return;
				}

				logger.warning("LuckPerms not ready yet. Attempt " + attempts[0] + " of " + MAX_RETRY_ATTEMPTS);
			}
		}, STARTUP_DELAY_MS, RETRY_DELAY_MS, TimeUnit.MILLISECONDS);
	}

	private void registerEventHooks() {
		List<PlayerDataType> types = new ArrayList<>(hook.getAutoLoadedDataTypes());
		if(!types.contains(PlayerDataType.NETWORK)) types.add(PlayerDataType.NETWORK);

		hook.registerJoinCallback(player -> {
			types.forEach(type -> getPlayerData(player.getUuid(), type));

			NetworkData data = getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
			if(data == null) throw new RuntimeException("Critical Error: Player data not found (This is bad)");
			data.setName(player.getName());
		});

		hook.registerQuitCallback(player -> {
			if(!loadedPlayerData.containsKey(player.getUuid())) return;
			loadedPlayerData.get(player.getUuid()).forEach(PlayerDataDocument::save);
			unloadPlayerData(player.getUuid());
		});
	}

	private static final Map<UUID, List<PlayerDataDocument>> loadedPlayerData = new HashMap<>();

	public static <T extends PlayerDataDocument> T getTemporaryPlayerData(String name, PlayerDataType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), documentClass);

		Pattern pattern = Pattern.compile("^" + Pattern.quote(name) + "$", Pattern.CASE_INSENSITIVE);

		return collection.find(regex("name", pattern)).first();
	}

	public static <T extends PlayerDataDocument> T getTemporaryPlayerData(UUID uuid, PlayerDataType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

		if(loadedPlayerData.containsKey(uuid)) {
			for(PlayerDataDocument document : loadedPlayerData.get(uuid)) {
				if(document.getClass().equals(type.getDocumentClass())) return (T) document;
			}
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), documentClass);

		return collection.find(eq("uuid", uuid.toString())).first();
	}

	public static <T extends PlayerDataDocument> T getPlayerData(UUID uuid, PlayerDataType type) {
		T document = getTemporaryPlayerData(uuid, type);
		if(document != null) {
			loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());
			loadedPlayerData.get(uuid).add(document);
			return document;
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		try {
			PlayerDataDocument newInstance = documentClass.newInstance();
			newInstance.setUuid(uuid.toString());
			loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());
			loadedPlayerData.get(uuid).add(newInstance);
			newInstance.save();
			return (T) newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void unloadPlayerData(UUID uuid) {
		loadedPlayerData.remove(uuid);
	}

	public static <T extends PlayerDataDocument> void savePlayerData(T document, PlayerDataType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

		MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), (Class<T>) type.getDocumentClass());
		collection.replaceOne(eq("uuid", document.getUuid()), document, new ReplaceOptions().upsert(true));
	}

	public LuckPerms getLuckPerms() {
		if (luckPerms == null) {
			throw new IllegalStateException("LuckPerms is not yet initialized");
		}
		return luckPerms;
	}

	public void shutdown() {
		scheduler.shutdownNow();
		try {
			scheduler.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}