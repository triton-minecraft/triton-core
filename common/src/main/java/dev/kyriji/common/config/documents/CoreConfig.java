package dev.kyriji.common.config.documents;

import dev.kyriji.common.config.models.ConfigDocument;
import dev.kyriji.common.config.models.MongoConnection;
import dev.kyriji.common.config.models.RedisConnection;
import dev.kyriji.common.config.models.SqlConnection;

public class CoreConfig extends ConfigDocument {

	MongoConnection playerDataConnection = new MongoConnection();
	SqlConnection luckPermsConnection = new SqlConnection();
	RedisConnection redisConnection = new RedisConnection();

	String velocitySecret = "EXAMPLE_SECRET";


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

	public RedisConnection getRedisConnection() {
		return redisConnection;
	}

	public void setRedisConnection(RedisConnection redisConnection) {
		this.redisConnection = redisConnection;
	}

	public String getVelocitySecret() {
		return velocitySecret;
	}

	public void setVelocitySecret(String velocitySecret) {
		this.velocitySecret = velocitySecret;
	}
}
