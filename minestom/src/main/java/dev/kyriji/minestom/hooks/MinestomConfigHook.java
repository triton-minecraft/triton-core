package dev.kyriji.minestom.hooks;

import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.minestom.controllers.ConfigManager;

public class MinestomConfigHook implements TritonConfigHook {
	@Override
	public String getConfigValue(String key) {
		return ConfigManager.getValue(key);
	}
}
