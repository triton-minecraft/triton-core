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
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.punishments.models.PunishmentAction;
import dev.kyriji.common.punishments.models.TimedPunishmentAction;
import dev.kyriji.common.punishments.utils.PunishmentUtils;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.*;

public class ReplyCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "r";
	}

	@Override
	public String getDescription() {
		return "Reply to the last person who messaged you";
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
		return List.of();
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		NetworkData playerData = PlayerDataManager.getPlayerData(sender.getUuid(), PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		if(args.length < 1) {
			sender.sendMessage(chatManager.formatMessage("&cUsage: /r <message>"));
			return;
		}

		if(playerData.getLastPrivateMessageSender() == null) {
			sender.sendMessage(chatManager.formatMessage("&cNo one has messaged you recently"));
			return;
		}

		UUID recipientUUID = UUID.fromString(playerData.getLastPrivateMessageSender());

		if(!isRecipientOnline(recipientUUID)) {
			sender.sendMessage(chatManager.formatMessage("&cThis player is no longer online"));
			return;
		}

		if(sender instanceof TritonPlayer) {
			PunishmentAction punishmentAction = PunishmentUtils.getActiveMute((TritonPlayer) sender);

			if(punishmentAction instanceof TimedPunishmentAction) {
				sender.sendMessage(chatManager.formatMessage(PunishmentUtils.getMuteMessage((TimedPunishmentAction) punishmentAction)));
				return;
			}
		}

		chatManager.getPrivateMessageManager().sendPrivateMessage(sender, recipientUUID, buildMessage(args));

		notifySender(sender, recipientUUID, buildMessage(args), chatManager);
	}

	private String buildMessage(String[] args) {
		return String.join(" ", Arrays.stream(args)
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

	private boolean isRecipientOnline(UUID recipientUUID) {
		return BigMinecraftAPI.getNetworkManager().getPlayers().containsKey(recipientUUID);
	}
}
