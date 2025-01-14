package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.models.TritonPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;

public class ChatManager {
	private final TritonChatHook hook;

	public ChatManager(TritonChatHook hook) {
		this.hook = hook;

		hook.registerChatCallback((player, message) -> formatPlayerName(player) + ": " + formatMessage(message));
	}

	public String formatPlayerName(TritonPlayer player) {
		LuckPerms api = TritonCoreCommon.INSTANCE.getPlayerDataManager().getLuckPerms();
		if(api == null) throw new NullPointerException("LuckPerms API not found");

		String groupName = api.getUserManager().getUser(player.getUuid()).getCachedData().getMetaData().getPrimaryGroup();
		if(groupName == null) throw new NullPointerException("Primary group not found");

		Group group = api.getGroupManager().getGroup(groupName);
		if(group == null) throw new NullPointerException("Group not found");

		String prefix = formatMessage("&7[" + group.getDisplayName() + "&7] ");
		String name = formatMessage(group.getCachedData().getMetaData().getPrefix() + player.getName() + "&r");

		return prefix + name;
	}

	public String formatMessage(String message) {
		return hook.formatMessage(message);
	}
}
