package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;

import java.util.List;

public class ReplyCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "r";
	}

	@Override
	public String getDescription() {
		return "Reply to the last person who messaged you";
	}

	@Override
	public CommandType getType() {
		return null;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return List.of();
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {

	}
}
