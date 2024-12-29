package dev.kyriji.common.playerdata.model;

import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

@BsonDiscriminator
public abstract class PlayerDataDocument {
	protected String uuid;

	public PlayerDataDocument() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void save() {
		PlayerDataManager.savePlayerData(this, Objects.requireNonNull(PlayerDataType.fromClass(this.getClass())));
	}
}