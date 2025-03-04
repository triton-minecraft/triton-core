package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;

import java.util.Arrays;
import java.util.List;

public class LobbyCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "lobby";
	}

	@Override
	public List<String> getAliases() {
		return List.of("l", "hub");
	}

	@Override
	public String getDescription() {
		return "A test command";
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PROXY;
	}

	@Override
	public ExecutorType getExecutorType() {
		return ExecutorType.PLAYER;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return List.of();
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		TritonPlayer player = (TritonPlayer) sender;
		BigMinecraftAPI.getNetworkManager().queuePlayer(player.getUuid(), "lobby");
	}

}
