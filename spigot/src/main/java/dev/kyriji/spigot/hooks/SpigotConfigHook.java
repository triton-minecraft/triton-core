package dev.kyriji.spigot.hooks;

import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.controllers.ConfigManager;

public class SpigotConfigHook implements TritonConfigHook {
	@Override
	public String getConfigValue(String key) {
		return ConfigManager.getValue(key);
	}
}
