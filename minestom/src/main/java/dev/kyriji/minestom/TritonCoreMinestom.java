package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonHook;

public class TritonCoreMinestom {

	public static void init() {
		TritonHook hook = new TritonHook() {
			@Override
			public void registerCommand(TritonCommand command) {

			}
		};

		TritonCoreCommon.init(hook);
	}
}