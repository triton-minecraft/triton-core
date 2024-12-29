package dev.kyriji.common.config.documents;

import dev.kyriji.common.config.models.ConfigDocument;

public class CoreConfig extends ConfigDocument {

	String mongoPlayerDataURI = "NULL";
	String mongoPlayerDataDatabase = "NULL";

	public String getMongoPlayerDataURI() {
		return mongoPlayerDataURI;
	}

	public void setMongoPlayerDataURI(String mongoPlayerDataURI) {
		this.mongoPlayerDataURI = mongoPlayerDataURI;
	}

	public String getMongoPlayerDataDatabase() {
		return mongoPlayerDataDatabase;
	}

	public void setMongoPlayerDataDatabase(String mongoPlayerDataDatabase) {
		this.mongoPlayerDataDatabase = mongoPlayerDataDatabase;
	}

}
