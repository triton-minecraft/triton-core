package dev.kyriji.minestom.hooks;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.punishments.utils.ReflectionUtils;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.ArgumentCallback;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientTabCompletePacket;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.DeclareCommandsPacket;
import net.minestom.server.network.packet.server.play.TabCompletePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MinestomCommandHook implements TritonCommandHook {
	public static Map<String, TritonCommand> registeredCommands = new HashMap<>();

	public MinestomCommandHook() {
		registerTabComplete();
	}

	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.SERVER && command.getCommandType() != CommandType.UNIVERSAL) return;

		Command commandInstance = new Command(command.getIdentifier(), command.getAliases().toArray(new String[0]));
		commandInstance.setDefaultExecutor((sender, context) -> {
			execute(command, sender, context);
		});

		Argument<String[]> argument = ArgumentType.StringArray("args")
				.setSuggestionCallback((sender, context, suggestion) -> {
					//This is simply here to trick the client into sending the tab complete packet so that we can intercept it and send our own.
				});

		commandInstance.addSyntax((sender, context) -> {
			execute(command, sender, context);

		}, argument);


		commandInstance.setCondition((sender, commandString) -> {
			if(!(sender instanceof Player player)) return true;
			MinestomPlayer minestomPlayer = new MinestomPlayer(player);
			if(command.getPermission() == null) return true;

			return minestomPlayer.hasPermission(command.getPermission().getIdentifier());
		});

		MinecraftServer.getCommandManager().register(commandInstance);
		registeredCommands.put(command.getIdentifier(), command);

	}

	public void execute(TritonCommand command, CommandSender sender, CommandContext context) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		MinestomCommandSender minestomSender;

		if(sender instanceof Player) minestomSender = new MinestomPlayer((Player) sender);
		else minestomSender = new MinestomCommandSender(sender);

		if(command.getExecutorType() != ExecutorType.ALL) {

			if(command.getExecutorType() == ExecutorType.PLAYER && !(sender instanceof Player)) {
				minestomSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by a player"));
				return;
			}

			if(command.getExecutorType() == ExecutorType.CONSOLE && sender instanceof Player) {
				minestomSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by console"));
				return;
			}
		}

		if(minestomSender instanceof MinestomPlayer player && command.getPermission() != null) {
			if(!player.hasPermission(command.getPermission().getIdentifier())) {
				player.sendMessage(chatManager.formatMessage("&cYou do not have permission to use this command"));
				return;
			}
		}

		String[] args = context.getInput().split(" ");
		command.execute(minestomSender, Arrays.copyOfRange(args, 1, args.length));


	}

	public void registerTabComplete() {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

		handler.addListener(EventListener.builder(PlayerPacketEvent.class)
				.handler(event -> {
					ClientPacket packet = event.getPacket();
					if (!(packet instanceof ClientTabCompletePacket(int transactionId, String text))) return;

					MinestomPlayer player = new MinestomPlayer(event.getPlayer());

					boolean endsWithSpace = text.endsWith(" ");
					String[] args = text.split(" ");

					if (endsWithSpace) {
						args = Arrays.copyOf(args, args.length + 1);
						args[args.length - 1] = "";
					}

					TritonCommand command = registeredCommands.get(args[0].replace("/", ""));
					if (command == null) return;

					args = Arrays.copyOfRange(args, 1, args.length);
					List<String> suggestions = command.getTabCompletions(player, args);

					int startIndex = text.lastIndexOf(' ') + 1;
					int argLength = text.length() - startIndex;

					sendTabCompletePacket(event.getPlayer(), transactionId, suggestions, startIndex, argLength);
				}).build()
		);
	}


	public void sendTabCompletePacket(Player player, int transactionId, List<String> suggestions, int start, int length) {
		List<TabCompletePacket.Match> matches = suggestions.stream()
				.map(suggestion -> new TabCompletePacket.Match(suggestion, null))
				.toList();

		TabCompletePacket packet = new TabCompletePacket(transactionId, start, length, matches);

		player.getPlayerConnection().sendPacket(packet);
	}
}
