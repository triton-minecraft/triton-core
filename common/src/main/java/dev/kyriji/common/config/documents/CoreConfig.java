package dev.kyriji.common.config.documents;

import dev.kyriji.common.config.models.ConfigDocument;
import dev.kyriji.common.config.models.MongoConnection;
import dev.kyriji.common.config.models.SqlConnection;

public class CoreConfig extends ConfigDocument {

	MongoConnection playerDataConnection = new MongoConnection();
	SqlConnection luckPermsConnection = new SqlConnection();

	public MongoConnection getPlayerDataConnection() {
		return playerDataConnection;
	}

	public void setPlayerDataConnection(MongoConnection playerDataConnection) {
		this.playerDataConnection = playerDataConnection;
	}

	public SqlConnection getLuckPermsConnection() {
		return luckPermsConnection;
	}

	public void setLuckPermsConnection(SqlConnection luckPermsConnection) {
		this.luckPermsConnection = luckPermsConnection;
	}
}
