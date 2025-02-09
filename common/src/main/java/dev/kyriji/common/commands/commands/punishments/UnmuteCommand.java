package dev.kyriji.common.commands.commands.punishments;

import dev.kyriji.common.chat.utils.ChatUtils;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.punishments.enums.suggestions.WarnReasonSuggestion;
import dev.kyriji.common.punishments.utils.PunishmentUtils;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.ArrayList;
import java.util.List;

public class UnmuteCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "unmute";
	}

	@Override
	public List<String> getAliases() {
		return List.of();
	}

	@Override
	public String getDescription() {
		return "Unmute a player from the network";
	}

	@Override
	public Permission getPermission() {
		return Permission.STAFF;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.UNIVERSAL;
	}

	@Override
	public ExecutorType getExecutorType() {
		return ExecutorType.ALL;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		if(args.length <= 1) {
			String hint = args.length == 0 ? "" : args[0];
			return new ArrayList<>(ChatUtils.getOnlinePlayerNames(hint));
		} else return List.of();
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {
		PunishmentUtils.applyPunishment(player, args, PunishmentType.UNMUTE);
	}
}
