package dev.kyriji.common.commands.models;

import dev.kyriji.common.model.TritonPlayer;

public abstract class TritonCommand {
	public abstract String getIdentifier();
	public abstract String getDescription();

	public abstract void execute(TritonPlayer player, String[] args);


}
