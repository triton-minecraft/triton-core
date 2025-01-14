package dev.kyriji.common.commands.commands;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonProfile;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.*;
import java.util.stream.Collectors;

public class MsgCommand extends TritonCommand {
	private static final String COMMAND_USAGE = "&cUsage: /msg <player> <message>";
	private static final String PLAYER_NOT_FOUND = "&cPlayer not found";
	private static final String REDIS_CHANNEL = "private-message";

	@Override
	public String getIdentifier() {
		return "msg";
	}

	@Override
	public String getDescription() {
		return "Private message another player";
	}

	@Override
	public CommandType getType() {
		return CommandType.SERVER;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		if (args.length == 1) {
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

		if (!validateArgs(sender, args, chatManager)) {
			return;
		}

		Optional<UUID> recipientOptional = findRecipient(args[0]);
		if (!recipientOptional.isPresent()) {
			sender.sendMessage(chatManager.formatMessage(PLAYER_NOT_FOUND));
			return;
		}

		UUID recipientId = recipientOptional.get();
		String message = buildMessage(args);

		sendPrivateMessage(sender, recipientId, message);
		notifySender(sender, recipientId, message, chatManager);
	}

	private boolean validateArgs(TritonCommandSender sender, String[] args, ChatManager chatManager) {
		if (args.length < 2) {
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

	private void sendPrivateMessage(TritonCommandSender sender, UUID recipientId, String message) {
		String redisMessage = String.format("%s %s %s %s",
				sender.getUuid().toString(),
				sender.getName(),
				recipientId.toString(),
				message);

		BigMinecraftAPI.getRedisManager().publish(REDIS_CHANNEL, redisMessage);
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
}