package dev.kyriji.common.commands.commands.punishments;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.punishments.models.PaginatedPunishments;
import dev.kyriji.common.punishments.utils.PunishmentUtils;

import java.util.List;

public class LogsCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "logs";
	}

	@Override
	public String getDescription() {
		return "Provides punishment logs for a player";
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
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(player instanceof TritonPlayer && !((TritonPlayer) player).hasPermission(Permission.STAFF.getIdentifier())) {
			player.sendMessage(chatManager.formatMessage("&cYou do not have permission to use this command"));
			return;
		}

		if(args.length == 0) {
			player.sendMessage(chatManager.formatMessage("&cUsage: /logs <player>"));
			return;
		}

		TritonProfile target = PunishmentUtils.findTargetPlayer(args[0]);

		if(target == null) {
			player.sendMessage(chatManager.formatMessage("&cPlayer not found"));
			return;
		}

		PaginatedPunishments paginatePunishments = PunishmentUtils.paginatePunishments(target);
		if(paginatePunishments == null) {
			player.sendMessage(chatManager.formatMessage("&cNo punishment logs found"));
			return;
		}

		int page = 1;
		if(args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				player.sendMessage(chatManager.formatMessage("&cInvalid page number"));
				return;
			}
		}

		if(paginatePunishments.isValidPage(page)) {
			player.sendMessage(chatManager.formatMessage("&cInvalid page number &7(1-" + paginatePunishments.getTotalPages() + ")"));
			return;
		}

		List<String> pageContent = paginatePunishments.getPageContent(page);
		for(String line : pageContent) {
			player.sendMessage(line);
		}

		player.sendMessage(chatManager.formatMessage("&7Use &f&o/logs " + target.getName() + " <page> &7to view more pages"));
	}
}
