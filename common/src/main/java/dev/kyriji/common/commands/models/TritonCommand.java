package dev.kyriji.common.commands.models;

import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.model.TritonCommandSender;
import dev.kyriji.common.model.TritonPlayer;

public abstract class TritonCommand {
	public abstract String getIdentifier();
	public abstract String getDescription();

	public abstract void execute(TritonCommandSender player, String[] args);


}
