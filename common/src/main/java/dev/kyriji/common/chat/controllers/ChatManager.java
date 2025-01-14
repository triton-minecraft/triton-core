package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.controllers.RedisListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChatManager {
	private final TritonChatHook hook;

	public ChatManager(TritonChatHook hook) {
		this.hook = hook;
	}

	public void init() {
		hook.registerChatCallback((player, message) -> formatPlayerName(player) + formatMessage("&7: " + message));

		//TODO: Move channel to enum
		BigMinecraftAPI.getRedisManager().addListener(new RedisListener("private-message") {
			@Override
			public void onMessage(String s) {
				System.out.println("Received messageEEEEEEEEEEE: " + s);

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

				List<TritonPlayer> onlinePlayers = hook.getOnlinePlayers();
				System.out.println("Online players: " + onlinePlayers);

				UUID RecipientUUID = UUID.fromString(s.split(" ")[2]);
				TritonPlayer recipient = onlinePlayers.stream().filter(player -> player.getUuid()
						.equals(RecipientUUID)).findFirst().orElse(null);

				if(recipient == null) return;

				String message = String.join(" ", Arrays.copyOfRange(s.split(" "), 3, s.split(" ").length));
				recipient.sendMessage(getFormattedPrivateMessage(sender, recipient, message));
			}
		});
	}

	public String getFormattedPrivateMessage(TritonProfile sender, TritonProfile recipient, String message) {
		return formatMessage(formatPlayerName(sender) + "&7 -> " + formatPlayerName(recipient) + "&7: " + formatMessage(message));
	}

	public String formatPlayerName(TritonProfile player) {
		LuckPerms api = TritonCoreCommon.INSTANCE.getPlayerDataManager().getLuckPerms();
		if (api == null) throw new NullPointerException("LuckPerms API not found");

		User user = api.getUserManager().getUser(player.getUuid());

		if (user == null) {
			try {
				user = api.getUserManager().loadUser(player.getUuid()).join();
			} catch (Exception e) {
				e.printStackTrace();
				return formatMessage("&7" + player.getName() + "&r");
			}
		}

		if (user == null) {
			return formatMessage("&7" + player.getName() + "&r");
		}

		String groupName = user.getPrimaryGroup();
		Group group = api.getGroupManager().getGroup(groupName);

		if (group == null) {
			return formatMessage("&7" + player.getName() + "&r");
		}

		String prefix = formatMessage("&7[" + group.getDisplayName() + "&7] ");
		String name = formatMessage(group.getCachedData().getMetaData().getPrefix() + player.getName() + "&r");

		return prefix + name;
	}

	public String formatMessage(String message) {
		return hook.formatMessage(message);
	}
}
