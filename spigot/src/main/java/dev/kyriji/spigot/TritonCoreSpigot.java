package dev.kyriji.spigot;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonHook;
import dev.kyriji.spigot.implementation.SpigotCommandSender;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCoreSpigot extends JavaPlugin {

	@Override
	public void onEnable() {

		TritonHook hook = new TritonHook() {
			@Override
			public void registerCommand(TritonCommand command) {
				if(command.getType() != CommandType.SERVER) return;

				PluginCommand spigotCommand = TritonCoreSpigot.this.getCommand(command.getIdentifier());
				if(spigotCommand == null) throw new NullPointerException("Command " + command.getIdentifier() + " failed to register");

				spigotCommand.setExecutor((sender, cmd, label, args) -> {
					SpigotCommandSender commandSender = new SpigotCommandSender(sender);
					command.execute(commandSender, args);
					return false;
				});
			}
		};

		TritonCoreCommon.init(hook);
	}
}
