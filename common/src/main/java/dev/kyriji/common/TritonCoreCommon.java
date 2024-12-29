package dev.kyriji.common;

import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;

public class TritonCoreCommon {
	public static TritonHook baseHook;

	public static void init(TritonHook hook) {
		baseHook = hook;

		CommandManager.init(hook);
		PlayerDataManager.init(hook);
	}
}