package dev.kyriji.common.tab.controllers;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class TabListManager {

	public TabListManager(TritonTabListHook hook) {
		hook.registerJoinCallback((player, players) -> {
			ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
			if(chatManager == null) throw new NullPointerException("Chat manager not found");

			List<TabPlayer> tabPlayers = new ArrayList<>();
			players.forEach(tritonPlayer -> {
				tabPlayers.add(new TabPlayer(tritonPlayer.getUuid(), getDisplayName(tritonPlayer), getPriority(tritonPlayer)));
			});

			hook.sendExistingPlayerData(player, tabPlayers);
			hook.sendTabListHeaderFooter(player, chatManager.formatMessage(getHeader()), chatManager.formatMessage(getFooter()));

			//TODO: Check for config value


			hook.updatePlayerPriority(player, getPriority(player));
			hook.updatePlayerDisplayName(player, getDisplayName(player));
		});
	}

	private String getHeader() {
		return "&6&lTriton&3&lMC";
	}

	private String getFooter() {
		return "&btritonmc.com";
	}

	private int getPriority(TritonPlayer player) {
		PlayerDataManager playerDataManager = TritonCoreCommon.INSTANCE.getPlayerDataManager();
		User luckPermsUser = playerDataManager.getLuckPermsUser(player);

		if(luckPermsUser == null) return 0;

		String primaryGroup = luckPermsUser.getPrimaryGroup();
		Group group = playerDataManager.getLuckPermsGroup(primaryGroup);

		if(group == null) return 0;

		return group.getWeight().orElse(0);
	}

	private String getDisplayName(TritonPlayer player) {
		PlayerDataManager playerDataManager = TritonCoreCommon.INSTANCE.getPlayerDataManager();
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		String defaultName = chatManager.formatMessage("&7" + player.getName() + "&r");

		User luckPermsUser = playerDataManager.getLuckPermsUser(player);
		if(luckPermsUser == null) return defaultName;

		String primaryGroup = luckPermsUser.getPrimaryGroup();
		Group group = playerDataManager.getLuckPermsGroup(primaryGroup);

		if(group == null) return defaultName;

		return group.getCachedData().getMetaData().getPrefix() + player.getName() + "&r";
	}





}
