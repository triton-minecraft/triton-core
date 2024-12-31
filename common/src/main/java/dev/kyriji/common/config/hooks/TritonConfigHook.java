package dev.kyriji.common.config.hooks;

import dev.kyriji.common.models.TritonHook;

public interface TritonConfigHook extends TritonHook {
	String getConfigValue(String key);
}
