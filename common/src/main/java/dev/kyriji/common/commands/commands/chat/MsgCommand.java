package dev.kyriji.common.commands.commands.chat;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.PunishmentData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.punishments.models.PunishmentAction;
import dev.kyriji.common.punishments.models.TimedPunishmentAction;
import dev.kyriji.common.punishments.utils.PunishmentUtils;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.*;
import java.util.stream.Collectors;

public class MsgCommand extends TritonCommand {
	private static final String COMMAND_USAGE = "&cUsage: /msg <player> <message>";
	private static final String PLAYER_NOT_FOUND = "&cPlayer not found";
	private static final String SELF_MESSAGE = "&cYou can't message yourself";

	@Override
	public String getIdentifier() {
		return "msg";
	}

	@Override
	public String getDescription() {
		return "Private message another player";
	}

	@Override
	public Permission getPermission() {
		return null;
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
		if(args.length == 1) {
			String partial = args[0].toLowerCase();
			return BigMinecraftAPI.getNetworkManager().getPlayers().values().stream()
					.filter(name -> name.toLowerCase().startsWith(partial))
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(!validateArgs(sender, args, chatManager)) {
			return;
		}

		Optional<UUID> recipientOptional = findRecipient(args[0]);
		if(!recipientOptional.isPresent()) {
			sender.sendMessage(chatManager.formatMessage(PLAYER_NOT_FOUND));
			return;
		}

		if(recipientOptional.get().equals(sender.getUuid())) {
			sender.sendMessage(chatManager.formatMessage(SELF_MESSAGE));
			return;
		}

		UUID recipientId = recipientOptional.get();
		String message = buildMessage(args);

		if(sender instanceof TritonPlayer) {
			PunishmentAction punishmentAction = PunishmentUtils.getActiveMute((TritonPlayer) sender);

			if(punishmentAction instanceof TimedPunishmentAction) {
				sender.sendMessage(chatManager.formatMessage(PunishmentUtils.getMuteMessage((TimedPunishmentAction) punishmentAction)));
				return;
			}
		}

		chatManager.getPrivateMessageManager().sendPrivateMessage(sender, recipientId, message);
		notifySender(sender, recipientId, message, chatManager);
	}

	private boolean validateArgs(TritonCommandSender sender, String[] args, ChatManager chatManager) {
		if(args.length < 2) {
			sender.sendMessage(chatManager.formatMessage(COMMAND_USAGE));
			return false;
		}
		return true;
	}

	private Optional<UUID> findRecipient(String targetName) {
		return BigMinecraftAPI.getNetworkManager().getPlayers().entrySet().stream()
				.filter(entry -> entry.getValue().equalsIgnoreCase(targetName))
				.map(Map.Entry::getKey)
				.findFirst();
	}

	private String buildMessage(String[] args) {
		return String.join(" ", Arrays.stream(args)
				.skip(1)
				.toArray(String[]::new));
	}

	private void notifySender(TritonCommandSender sender, UUID recipientId, String message, ChatManager chatManager) {
		TritonProfile recipientProfile = createRecipientProfile(recipientId);
		sender.sendMessage(chatManager.getPrivateMessageManager().getFormattedPrivateMessage(sender, recipientProfile, message));
	}

	private TritonProfile createRecipientProfile(UUID recipientId) {
		return new TritonProfile() {
			@Override
			public UUID getUuid() {
				return recipientId;
			}

			@Override
			public String getName() {
				return BigMinecraftAPI.getNetworkManager().getPlayers().get(recipientId);
			}
		};
	}
}