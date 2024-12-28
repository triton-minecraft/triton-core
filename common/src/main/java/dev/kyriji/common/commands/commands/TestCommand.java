package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonCommandSender;
import dev.kyriji.common.model.TritonPlayer;

public class TestCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "A test command";
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		sender.sendMessage("Hello world!");
	}

}
