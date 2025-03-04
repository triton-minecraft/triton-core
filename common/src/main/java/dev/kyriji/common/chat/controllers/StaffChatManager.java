package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;
import dev.kyriji.bigminecraftapi.controllers.RedisListener;

public class StaffChatManager {

	private static final String REDIS_CHANNEL = "staff-chat";
	private final TritonChatHook chatHook;
	private final ChatManager chatManager;

	public StaffChatManager(TritonChatHook chatHook, ChatManager chatManager) {
		this.chatHook = chatHook;
		this.chatManager = chatManager;

		registerStaffMessageListener();
	}

	public void sendStaffMessage(String message) {
		BigMinecraftAPI.getRedisManager().publish(REDIS_CHANNEL, message);
	}

	private String formatStaffChatMessage(String message) {
		String prefix = chatManager.formatMessage("&7[&cSTAFF&7] &7");
		return prefix + message;
	}

	private void registerStaffMessageListener() {
		if(TritonCoreCommon.SERVER_TYPE == ServerType.VELOCITY) return;

		//TODO: Move channel to enum
		BigMinecraftAPI.getRedisManager().addListener(new RedisListener(REDIS_CHANNEL) {
			@Override
			public void onMessage(String s) {
				String formattedMessage = formatStaffChatMessage(s);

				chatHook.getOnlinePlayers().forEach(player -> {
					if(player.hasPermission(Permission.STAFF.getIdentifier())) player.sendMessage(formattedMessage);
				});
			}
		});
	}
}
