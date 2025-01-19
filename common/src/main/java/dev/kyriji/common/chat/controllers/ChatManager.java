package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.commands.commands.IgnoreCommand;
import dev.kyriji.common.commands.commands.MsgCommand;
import dev.kyriji.common.commands.commands.ReplyCommand;
import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.List;
import java.util.UUID;

public class ChatManager {
	private final TritonChatHook hook;
	private PrivateMessageManager privateMessageManager;
	private StaffChatManager staffChatManager;

	public ChatManager(TritonChatHook hook) {
		this.hook = hook;
	}

	public void init() {
		CommandManager commandManager = TritonCoreCommon.INSTANCE.getCommandManager();
		if(commandManager == null) throw new NullPointerException("The command feature must be registered to use this feature");

		commandManager.registerCommand(new MsgCommand());
		commandManager.registerCommand(new ReplyCommand());
		commandManager.registerCommand(new IgnoreCommand());

		hook.registerChatCallback(new ChatProvider() {
			@Override
			public String work(TritonPlayer player, String message) {
				return formatPlayerName(player) + formatMessage("&7: " + message);
			}

			@Override
			public List<UUID> getRecipients(TritonPlayer player, List<UUID> onlinePlayers) {

				onlinePlayers.removeIf(uuid -> isIgnored(uuid, player.getUuid()));
				return onlinePlayers;
			}
		});

		privateMessageManager = new PrivateMessageManager(hook, this);
		staffChatManager = new StaffChatManager(hook, this);
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

	public boolean isIgnored(UUID player, UUID target) {
		NetworkData playerData = PlayerDataManager.getTemporaryPlayerData(player, PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		return playerData.getIgnoredPlayers().contains(target.toString());
	}

	public PrivateMessageManager getPrivateMessageManager() {
		return privateMessageManager;
	}

	public StaffChatManager getStaffChatManager() {
		return staffChatManager;
	}
}
