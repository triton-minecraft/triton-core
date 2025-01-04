package dev.kyriji.spigot.hooks;

import dev.kyriji.common.commands.enums.CommandType;
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
		if(command.getType() != CommandType.SERVER) return;

		PluginCommand spigotCommand = TritonCoreSpigot.INSTANCE.getCommand(command.getIdentifier());
		if(spigotCommand == null) throw new NullPointerException("Command " + command.getIdentifier() + " failed to register");

		spigotCommand.setExecutor((sender, cmd, label, args) -> {
			SpigotCommandSender commandSender;

			if(sender instanceof Player player) commandSender = new SpigotPlayer(player);
			else commandSender = new SpigotCommandSender(sender);

			command.execute(commandSender, args);
			return false;
		});
	}
}
