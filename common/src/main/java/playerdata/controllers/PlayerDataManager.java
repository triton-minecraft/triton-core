package playerdata.controllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.json.JsonWriterSettings;
import playerdata.documents.NetworkDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class PlayerDataManager {

	public static void main(String[] args) {
		init();
	}

	public static void init() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		String connectionString = "";

		MongoClientSettings settings = MongoClientSettings.builder()
				.codecRegistry(pojoCodecRegistry)
				.applyConnectionString(new ConnectionString(connectionString))
				.build();

		try (MongoClient mongoClient = MongoClients.create(settings)) {
			System.out.println("=> Print list of databases:");
			MongoDatabase database = mongoClient.getDatabase("pit").withCodecRegistry(pojoCodecRegistry);

			MongoCollection<NetworkDocument> collection = database.getCollection("users", NetworkDocument.class);

//			NetworkDocument networkDocument = new NetworkDocument(UUID.randomUUID(), "John Doe");
//			collection.insertOne(networkDocument);

			NetworkDocument foundUser = collection.find(eq("name", "John Doe")).first();
			System.out.println(foundUser.getUuid());

		}


	}

}
