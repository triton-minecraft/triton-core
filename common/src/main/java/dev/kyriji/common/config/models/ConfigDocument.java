package dev.kyriji.common.config.models;

import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.enums.ConfigType;

public abstract class ConfigDocument {
	protected ConfigType type;

	public ConfigDocument() {
	}

	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}

	public void save() {
		ConfigManager.saveConfig(this);
	}

}
