package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.playerdata.enums.Permission;

import java.util.Arrays;
import java.util.List;

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
	public Permission getPermission() {
		return Permission.STAFF;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PROXY;
	}

	@Override
	public ExecutorType getExecutorType() {
		return ExecutorType.ALL;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return Arrays.asList("test12", "test22", "test32");
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		sender.sendMessage("Hello world from proxy!");
		sender.sendMessage(Arrays.toString(args));
	}

}
