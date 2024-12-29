package dev.kyriji.common.config.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.common.config.models.ConfigDocument;
import dev.kyriji.common.database.controllers.DatabaseManager;
import dev.kyriji.common.database.enums.DatabaseType;
import dev.kyriji.common.database.records.DatabaseConnection;
import dev.kyriji.common.models.TritonHook;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class ConfigManager {
	public static final String CONFIG_COLLECTION = "config";
	private static final Map<ConfigType, ConfigDocument> loadedConfigs = new HashMap<>();

	public static void init(TritonHook hook) {
		String mongoConfigURI = hook.getConfigValue("mongoConfigURI");
		String mongoDatabase = hook.getConfigValue("mongoConfigDatabase");

		if(mongoConfigURI == null || mongoDatabase == null) throw new NullPointerException("Mongo config URI or database cannot be null");

		DatabaseManager.addDatabase(DatabaseType.CONFIG, mongoConfigURI, mongoDatabase);
	}

	@SuppressWarnings("unchecked")
	public static <T extends ConfigDocument> T getConfig(ConfigType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.CONFIG);

		if(loadedConfigs.containsKey(type)) return (T) loadedConfigs.get(type);
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = connection.database().getCollection(CONFIG_COLLECTION, documentClass);

		T foundDocument = collection.find(eq("type", type)).first();

		if(foundDocument != null) {
			loadedConfigs.put(type, foundDocument);
			return foundDocument;
		}

		try {
			ConfigDocument newInstance = documentClass.newInstance();
			newInstance.setType(type);
			loadedConfigs.put(type, newInstance);
			newInstance.save();
			return (T) newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T extends ConfigDocument> void saveConfig(T document) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.CONFIG);
		ConfigType type = document.getType();

		MongoCollection<T> collection = connection.database().getCollection(CONFIG_COLLECTION, (Class<T>) type.getDocumentClass());

		collection.replaceOne(eq("type", type), document, new ReplaceOptions().upsert(true));
	}
}
