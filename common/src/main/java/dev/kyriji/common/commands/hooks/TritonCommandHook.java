package dev.kyriji.common.commands.hooks;

import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonHook;

public interface TritonCommandHook extends TritonHook {
	void registerCommand(TritonCommand command);
}
