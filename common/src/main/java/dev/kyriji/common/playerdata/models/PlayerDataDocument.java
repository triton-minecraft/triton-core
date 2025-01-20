package dev.kyriji.common.playerdata.models;

import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.Objects;

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