package dev.kyriji.common.playerdata.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.documents.CoreConfig;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.common.database.controllers.DatabaseManager;
import dev.kyriji.common.database.enums.DatabaseType;
import dev.kyriji.common.database.records.DatabaseConnection;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.model.PlayerDataDocument;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class PlayerDataManager {

	public static void init(TritonHook hook) {
		CoreConfig config = ConfigManager.getConfig(ConfigType.CORE);
		if(config == null) throw new NullPointerException("Core config not found");

		String mongoPlayerDataURI = config.getMongoPlayerDataURI();
		String mongoPlayerDataDatabase = config.getMongoPlayerDataDatabase();

		DatabaseManager.addDatabase(DatabaseType.PLAYER_DATA, mongoPlayerDataURI, mongoPlayerDataDatabase);
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
}
