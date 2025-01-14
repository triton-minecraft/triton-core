package dev.kyriji.common.commands.models;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.models.TritonCommandSender;

import java.util.List;

public abstract class TritonCommand {
	public abstract String getIdentifier();
	public abstract String getDescription();
	public abstract CommandType getType();

	public abstract List<String> getTabCompletions(TritonCommandSender sender, String[] args);

	public abstract void execute(TritonCommandSender player, String[] args);


}
