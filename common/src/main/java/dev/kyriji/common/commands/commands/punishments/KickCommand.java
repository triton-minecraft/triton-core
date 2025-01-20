package dev.kyriji.common.commands.commands.punishments;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.punishments.utils.PunishmentUtils;

import java.util.List;

public class KickCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "kick";
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
	public ExecutorType getExecutorType() {
		return ExecutorType.ALL;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return List.of();
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {
		PunishmentUtils.applyPunishment(player, args, PunishmentType.KICK);
	}
}
