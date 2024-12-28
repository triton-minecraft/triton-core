package dev.kyriji.common.model;

import dev.kyriji.common.commands.models.TritonCommand;

public abstract class TritonHook {

	public abstract void registerCommand(TritonCommand command);
}
