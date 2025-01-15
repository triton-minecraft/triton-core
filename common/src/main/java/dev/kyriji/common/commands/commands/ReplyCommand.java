package dev.kyriji.common.commands.commands;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
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
	public CommandType getType() {
		return null;
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

		if(args.length < 1) {
			player.sendMessage(chatManager.formatMessage("&cUsage: /r <message>"));
			return;
		}

		if(playerData.getLastPrivateMessageSender() == null) {
			player.sendMessage(chatManager.formatMessage("&cYou haven't messaged anyone recently"));
			return;
		}

		UUID recipientUUID = playerData.getLastPrivateMessageSender();

		if(!isRecipientOnline(recipientUUID)) {
			player.sendMessage(chatManager.formatMessage("&cThis player is no longer online"));
			return;
		}

		chatManager.sendPrivateMessage(player, recipientUUID, buildMessage(args));

		notifySender(player, recipientUUID, buildMessage(args), chatManager);
	}

	private String buildMessage(String[] args) {
		return String.join(" ", Arrays.stream(args)
				.skip(1)
				.toArray(String[]::new));
	}

	private void notifySender(TritonCommandSender sender, UUID recipientId, String message, ChatManager chatManager) {
		TritonProfile recipientProfile = createRecipientProfile(recipientId);
		sender.sendMessage(chatManager.getFormattedPrivateMessage(sender, recipientProfile, message));
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
