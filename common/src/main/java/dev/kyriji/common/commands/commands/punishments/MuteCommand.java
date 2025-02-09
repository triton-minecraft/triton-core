package dev.kyriji.common.commands.commands.punishments;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.chat.utils.ChatUtils;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.PunishmentData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.utils.PlayerDataUtils;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.punishments.enums.suggestions.BanReasonSuggestion;
import dev.kyriji.common.punishments.enums.suggestions.DurationSuggestion;
import dev.kyriji.common.punishments.enums.suggestions.MuteReasonSuggestion;
import dev.kyriji.common.punishments.models.TimedPunishmentAction;
import dev.kyriji.common.punishments.utils.PunishmentUtils;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MuteCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "mute";
	}

	@Override
	public List<String> getAliases() {
		return List.of();
	}

	@Override
	public String getDescription() {
		return "Mute a player from the network";
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
		} else if(args.length == 2) {
			return DurationSuggestion.toList();
		} else {
			String[] newArgs = Arrays.copyOfRange(args, 2, args.length);
			return MuteReasonSuggestion.toList(String.join(" ", newArgs));
		}
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {
		PunishmentUtils.applyPunishment(player, args, PunishmentType.MUTE);
	}
}
