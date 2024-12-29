package dev.kyriji.common.playerdata.controllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.model.PlayerDataDocument;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PlayerDataManager {

	public static MongoClient mongoClient;
	public static MongoDatabase database;

	private static final Map<UUID, List<PlayerDataDocument>> loadedPlayerData = new HashMap<>();

	public static void init(TritonHook hook) {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		String connectionString = "";

		MongoClientSettings settings = MongoClientSettings.builder()
				.codecRegistry(pojoCodecRegistry)
				.applyConnectionString(new ConnectionString(connectionString))
				.build();

		mongoClient = MongoClients.create(settings);
		database = mongoClient.getDatabase("pit").withCodecRegistry(pojoCodecRegistry);

	}

	public static <T extends PlayerDataDocument> T getPlayerData(UUID uuid, PlayerDataType type) {
		loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());

		for(PlayerDataDocument document : loadedPlayerData.get(uuid)) {
			if(document.getClass().equals(type.getDocumentClass())) return (T) document;
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = database.getCollection(type.getCollectionName(), documentClass);

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
		MongoCollection<T> collection = database.getCollection(type.getCollectionName(), (Class<T>) type.getDocumentClass());
		collection.replaceOne(eq("uuid", document.getUuid()), document, new ReplaceOptions().upsert(true));
	}
}
