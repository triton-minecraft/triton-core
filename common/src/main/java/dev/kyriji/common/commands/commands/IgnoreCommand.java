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
import net.luckperms.api.LuckPerms;

import java.util.List;
import java.util.UUID;

public class IgnoreCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "ignore";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public CommandType getType() {
		return CommandType.SERVER;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return List.of();
	}

	@Override
	public void execute(TritonCommandSender player, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(args.length < 1) {
			player.sendMessage(chatManager.formatMessage("&cUsage: /ignore <add/remove/list>"));
			return;
		}

		IgnoreAction action;

		try {
			action = IgnoreAction.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			player.sendMessage(chatManager.formatMessage("&cInvalid ignore action. <add/remove/list>"));
			return;
		}

		switch (action) {
			case ADD:
				addIgnore(player, args);
				break;
			case REMOVE:
				removeIgnore(player, args);
				break;
			case LIST:
				listIgnores(player, args);
				break;
		}
	}

	private void addIgnore(TritonCommandSender player, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(args.length < 2) {
			player.sendMessage(chatManager.formatMessage("&cUsage: /ignore add <player>"));
			return;
		}

		String targetString = args[1];
		TritonProfile target = loadUser(targetString);

		if(target == null) {
			player.sendMessage(chatManager.formatMessage("&cPlayer not found"));
			return;
		}

		if(target.getUuid().equals(player.getUuid())) {
			player.sendMessage(chatManager.formatMessage("&cYou cannot ignore yourself"));
			return;
		}

		NetworkData playerData = PlayerDataManager.getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		List<String> ignoredPlayers = playerData.getIgnoredPlayers();

		if(ignoredPlayers.contains(target.getUuid().toString())) {
			player.sendMessage(chatManager.formatMessage("&cYou are already ignoring this player"));
			return;
		}

		ignoredPlayers.add(target.getUuid().toString());
		player.sendMessage(chatManager.formatMessage("&aYou are now ignoring " + chatManager.formatPlayerName(target)));
	}

	private void removeIgnore(TritonCommandSender player, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(args.length < 2) {
			player.sendMessage(chatManager.formatMessage("&cUsage: /ignore remove <player>"));
			return;
		}

		String targetString = args[1];
		TritonProfile targetUser = loadUser(targetString);

		NetworkData playerData = PlayerDataManager.getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		if(targetUser == null) {
			player.sendMessage(chatManager.formatMessage("&cPlayer not found"));
			return;
		}

		List<String> ignoredPlayers = playerData.getIgnoredPlayers();

		if(!ignoredPlayers.contains(targetUser.getUuid().toString())) {
			player.sendMessage(chatManager.formatMessage("&cYou are not ignoring this player"));
			return;
		}

		ignoredPlayers.remove(targetUser.getUuid().toString());
		player.sendMessage(chatManager.formatMessage("&aYou are no longer ignoring " + chatManager.formatPlayerName(targetUser)));
	}

	private void listIgnores(TritonCommandSender player, String[] args) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		NetworkData playerData = PlayerDataManager.getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
		if(playerData == null) throw new RuntimeException("Player data not found");

		List<String> ignoredPlayers = playerData.getIgnoredPlayers();

		if(ignoredPlayers.isEmpty()) {
			player.sendMessage(chatManager.formatMessage("&cYou are not ignoring any players"));
			return;
		}

		player.sendMessage(chatManager.formatMessage("&8&m------ &7Ignored players &8&m------"));
		ignoredPlayers.forEach(uuid -> {
			TritonProfile user = loadUser(UUID.fromString(uuid));

			if(user == null) return;
			player.sendMessage(chatManager.formatMessage("&7- ") + chatManager.formatPlayerName(user));
		});

		player.sendMessage(chatManager.formatMessage("&8&m---------------------------"));
	}

	private TritonProfile loadUser(String name) {
		LuckPerms luckPerms = TritonCoreCommon.INSTANCE.getPlayerDataManager().getLuckPerms();

		NetworkData playerData = PlayerDataManager.getTemporaryPlayerData(name, PlayerDataType.NETWORK);
		if(playerData == null) return null;

		return getProfile(playerData);
	}

	private TritonProfile loadUser(UUID uuid) {
		NetworkData playerData = PlayerDataManager.getTemporaryPlayerData(uuid, PlayerDataType.NETWORK);
		if(playerData == null) return null;

		return getProfile(playerData);
	}

	private TritonProfile getProfile(NetworkData data) {
		return new TritonProfile() {
			@Override
			public UUID getUuid() {
				return UUID.fromString(data.getUuid());
			}

			@Override
			public String getName() {
				return data.getName();
			}
		};
	}

	enum IgnoreAction {
		ADD,
		REMOVE,
		LIST
	}
}
