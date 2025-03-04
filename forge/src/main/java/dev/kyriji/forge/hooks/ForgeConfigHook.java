package dev.kyriji.forge.hooks;

import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.forge.controllers.ConfigManager;

public class ForgeConfigHook implements TritonConfigHook {
	@Override
	public String getConfigValue(String key) {
		return ConfigManager.getValue(key);
	}
}
