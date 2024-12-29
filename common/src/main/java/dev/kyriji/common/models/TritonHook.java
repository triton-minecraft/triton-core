package dev.kyriji.common.models;

import dev.kyriji.common.commands.models.TritonCommand;

public abstract class TritonHook {

	public abstract void registerCommand(TritonCommand command);

	public abstract String getConfigValue(String key);
}
