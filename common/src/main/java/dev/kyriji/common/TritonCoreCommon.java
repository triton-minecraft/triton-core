package dev.kyriji.common;

import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.model.TritonHook;

import java.util.List;

public class TritonCoreCommon {
	public static TritonHook baseHook;

	public static void init(TritonHook hook) {
		baseHook = hook;

		CommandManager.init(hook);
	}
}