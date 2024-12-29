package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;

import java.util.Arrays;
import java.util.List;

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
	public CommandType getType() {
		return CommandType.SERVER;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		System.out.println(Arrays.toString(args));
		return Arrays.asList("test1", "test2", "test3");
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		sender.sendMessage("Hello world!");
		sender.sendMessage(Arrays.toString(args));
	}

}
