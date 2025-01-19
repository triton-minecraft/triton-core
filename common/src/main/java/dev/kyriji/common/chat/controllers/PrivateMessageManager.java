package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.controllers.RedisListener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PrivateMessageManager {

	private static final String REDIS_CHANNEL = "private-message";

	private final TritonChatHook chatHook;
	private final ChatManager chatManager;

	public PrivateMessageManager(TritonChatHook chatHook, ChatManager chatManager) {
		this.chatHook = chatHook;
		this.chatManager = chatManager;

		registerPrivateMessageListener();
	}

	private void registerPrivateMessageListener() {
		//TODO: Move channel to enum
		BigMinecraftAPI.getRedisManager().addListener(new RedisListener(REDIS_CHANNEL) {
			@Override
			public void onMessage(String s) {
				TritonProfile sender = new TritonProfile() {
					@Override
					public UUID getUuid() {
						return UUID.fromString(s.split(" ")[0]);
					}

					@Override
					public String getName() {
						return s.split(" ")[1];
					}
				};

				List<TritonPlayer> onlinePlayers = chatHook.getOnlinePlayers();

				UUID RecipientUUID = UUID.fromString(s.split(" ")[2]);
				TritonProfile recipient = onlinePlayers.stream().filter(player -> player.getUuid()
						.equals(RecipientUUID)).findFirst().orElse(null);

				if(recipient == null) {
					String playerName = BigMinecraftAPI.getNetworkManager().getPlayers().get(RecipientUUID);
					if(playerName == null) return;

					recipient = new TritonProfile() {
						@Override
						public UUID getUuid() {
							return RecipientUUID;
						}

						@Override
						public String getName() {
							return playerName;
						}
					};
				}

				String message = getFormattedPrivateMessage(sender, recipient, String.join(" ", Arrays.copyOfRange(s.split(" "), 3, s.split(" ").length)));

				List<TritonPlayer> staff = onlinePlayers.stream().filter(player -> player.hasPermission(Permission.STAFF.getIdentifier())).toList();
				for(TritonPlayer tritonPlayer : staff) {
					NetworkData networkData = PlayerDataManager.getPlayerData(tritonPlayer.getUuid(), PlayerDataType.NETWORK);
					if(networkData == null) return;

					if(networkData.getStaffData().isSocialSpyEnabled()) tritonPlayer.sendMessage(formatSocialSpyMessage(message));
				}

				if(!(recipient instanceof TritonPlayer)) return;

				NetworkData networkData = PlayerDataManager.getPlayerData(recipient.getUuid(), PlayerDataType.NETWORK);
				if(networkData == null) return;

				if(chatManager.isIgnored(recipient.getUuid(), sender.getUuid())) return;

				((TritonPlayer) recipient).sendMessage(message);

				networkData.setLastPrivateMessageSender(sender.getUuid().toString());
			}
		});
	}

	public void sendPrivateMessage(TritonCommandSender sender, UUID recipientId, String message) {
		String redisMessage = String.format("%s %s %s %s",
				sender.getUuid().toString(),
				sender.getName(),
				recipientId.toString(),
				message);

		BigMinecraftAPI.getRedisManager().publish(REDIS_CHANNEL, redisMessage);
	}

	public String getFormattedPrivateMessage(TritonProfile sender, TritonProfile recipient, String message) {
		return chatManager.formatMessage(chatManager.formatPlayerName(sender) + "&7 -> "
				+ chatManager.formatPlayerName(recipient) + "&7: " + chatManager.formatMessage(message));
	}

	public String formatSocialSpyMessage(String message) {
		return chatManager.formatMessage("&7[&cSOCIALSPY&7] &7" + message);
	}
}
