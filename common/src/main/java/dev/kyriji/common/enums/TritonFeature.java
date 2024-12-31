package dev.kyriji.common.enums;

import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.models.TritonHook;

public enum TritonFeature {
	CONFIG(TritonConfigHook.class),
	COMMANDS(TritonCommandHook.class),
	PLAYER_DATA(null),
	INVENTORY(TritonInventoryHook.class),
	;

	public final Class<? extends TritonHook> hookClass;

	TritonFeature(Class<? extends TritonHook> hookClass) {
		this.hookClass = hookClass;
	}

	public Class<? extends TritonHook> getHookClass() {
		return hookClass;
	}
}
