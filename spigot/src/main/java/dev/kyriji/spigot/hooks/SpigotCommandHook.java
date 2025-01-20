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

public class SpigotCommandHook implements TritonCommandHook {
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
			}
			command.execute(commandSender, args);
			return false;
		});
	}
}
