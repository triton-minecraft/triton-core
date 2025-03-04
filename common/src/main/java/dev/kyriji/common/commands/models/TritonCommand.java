package dev.kyriji.common.commands.models;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.playerdata.enums.Permission;

import java.util.List;

public abstract class TritonCommand {
	public abstract String getIdentifier();
	public abstract List<String> getAliases();
	public abstract String getDescription();
	public abstract Permission getPermission();
	public abstract CommandType getCommandType();
	public abstract ExecutorType getExecutorType();


	public abstract List<String> getTabCompletions(TritonCommandSender sender, String[] args);

	public abstract void execute(TritonCommandSender player, String[] args);


}
