package dev.kyriji.velocity.hooks;

import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.velocity.controllers.ConfigManager;

public class VelocityConfigHook implements TritonConfigHook {
	@Override
	public String getConfigValue(String key) {
		return ConfigManager.getValue(key);
	}
}
