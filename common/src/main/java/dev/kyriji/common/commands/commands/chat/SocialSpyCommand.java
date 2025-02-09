package dev.kyriji.common.commands.commands.chat;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.List;

public class SocialSpyCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "socialspy";
	}

	@Override
	public List<String> getAliases() {
		return List.of("ss");
	}

	@Override
	public String getDescription() {
		return "Staff command to toggle social spy";
	}

	@Override
	public Permission getPermission() {
		return Permission.STAFF;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.SERVER;
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
	public void execute(TritonCommandSender player, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		NetworkData playerData = PlayerDataManager.getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		boolean currentStatus = playerData.getStaffData().isSocialSpyEnabled();
		playerData.getStaffData().setSocialSpyEnabled(!currentStatus);

		player.sendMessage(chatManager.formatMessage((currentStatus ? "&c" : "&a") + "Social spy " + (currentStatus ? "disabled" : "enabled")));
	}
}
