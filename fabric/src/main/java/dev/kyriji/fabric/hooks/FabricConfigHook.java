package dev.kyriji.fabric.hooks;

import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.fabric.controllers.ConfigManager;

public class FabricConfigHook implements TritonConfigHook {
	@Override
	public String getConfigValue(String key) {
		return ConfigManager.getValue(key);
	}
}
