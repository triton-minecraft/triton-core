package dev.kyriji.spigot.hooks;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.implementation.SpigotCommandSender;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpigotCommandHook implements TritonCommandHook, Listener {
	public static List<TritonCommand> registeredCommands = new ArrayList<>();

	public SpigotCommandHook() {
		Plugin plugin = TritonCoreSpigot.INSTANCE;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.SERVER && command.getCommandType() != CommandType.UNIVERSAL) return;

		PluginCommand spigotCommand = TritonCoreSpigot.INSTANCE.getCommand(command.getIdentifier());
		if(spigotCommand == null) throw new NullPointerException("Command " + command.getIdentifier() + " failed to register");

		spigotCommand.setExecutor((sender, cmd, label, args) -> {
			SpigotCommandSender commandSender;

			if(sender instanceof Player player) commandSender = new SpigotPlayer(player);
			else commandSender = new SpigotCommandSender(sender);

			if(command.getExecutorType() != ExecutorType.ALL) {
				ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

				if(command.getExecutorType() == ExecutorType.PLAYER && !(sender instanceof Player)) {
					commandSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by a player"));
					return false;
				}

				if(command.getExecutorType() == ExecutorType.CONSOLE && sender instanceof Player) {
					commandSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by console"));
					return false;
				}


				if(commandSender instanceof SpigotPlayer player && command.getPermission() != null) {
					if(!player.hasPermission(command.getPermission().getIdentifier())) {
						player.sendMessage(chatManager.formatMessage("&cYou do not have permission to execute this command"));
						return false;
					}
				}

			}
			command.execute(commandSender, args);
			return false;
		});

		registeredCommands.add(command);
	}

	@EventHandler
	public void onCommandSend(PlayerCommandSendEvent event) {
		event.getCommands().removeIf(command ->
				registeredCommands.stream()
						.filter(tritonCommand -> command.contains(tritonCommand.getIdentifier()))
						.anyMatch(tritonCommand ->
								tritonCommand.getPermission() != null &&
										!event.getPlayer().hasPermission(tritonCommand.getPermission().getIdentifier())
						)
		);
	}
}
