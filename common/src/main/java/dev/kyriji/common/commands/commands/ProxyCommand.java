package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonCommandSender;

import java.util.Arrays;

public class ProxyCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "proxy";
	}

	@Override
	public String getDescription() {
		return "A test command";
	}

	@Override
	public CommandType getType() {
		return CommandType.PROXY;
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		sender.sendMessage("Hello world from proxy!");
		sender.sendMessage(Arrays.toString(args));
	}

}
