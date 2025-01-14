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
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.model.PlayerDataDocument;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class PlayerDataManager {

	private static final int MAX_RETRY_ATTEMPTS = 30;
	private static final long RETRY_DELAY_MS = 1000;
	private static final long STARTUP_DELAY_MS = 1000;

	private static final Logger logger = Logger.getLogger(PlayerDataManager.class.getName());

	private LuckPerms luckPerms;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	public PlayerDataManager() {
		CoreConfig config = ConfigManager.getConfig(ConfigType.CORE);
		if(config == null) throw new NullPointerException("Core config not found");

		MongoConnection playerDataConnection = config.getPlayerDataConnection();
		DatabaseManager.addDatabase(DatabaseType.PLAYER_DATA, playerDataConnection.getUri(), playerDataConnection.getDatabase());

		initializeLuckPerms();
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

	private static final Map<UUID, List<PlayerDataDocument>> loadedPlayerData = new HashMap<>();

	public static <T extends PlayerDataDocument> T getPlayerData(UUID uuid, PlayerDataType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

		loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());

		for(PlayerDataDocument document : loadedPlayerData.get(uuid)) {
			if(document.getClass().equals(type.getDocumentClass())) return (T) document;
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), documentClass);

		T foundUser = collection.find(eq("uuid", uuid.toString())).first();

		if(foundUser != null) {
			loadedPlayerData.get(uuid).add(foundUser);
			return foundUser;
		}

		try {
			PlayerDataDocument newInstance = documentClass.newInstance();
			newInstance.setUuid(uuid.toString());
			loadedPlayerData.get(uuid).add(newInstance);
			return (T) newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
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