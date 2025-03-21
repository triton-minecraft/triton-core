package dev.kyriji.common.commands.commands.punishments;

import dev.kyriji.common.chat.utils.ChatUtils;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.punishments.enums.suggestions.BanReasonSuggestion;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.punishments.enums.suggestions.KickReasonSuggestion;
import dev.kyriji.common.punishments.utils.PunishmentUtils;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KickCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "kick";
	}

	@Override
	public List<String> getAliases() {
		return List.of();
	}

	@Override
	public String getDescription() {
		return "Kick a player from the network";
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.UNIVERSAL;
	}

	@Override
	public Permission getPermission() {
		return Permission.STAFF;
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
		} else {
			String[] newArgs = Arrays.copyOfRange(args, 2, args.length);
			return KickReasonSuggestion.toList(String.join(" ", newArgs));
		}
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {
		PunishmentUtils.applyPunishment(player, args, PunishmentType.KICK);
	}
}
