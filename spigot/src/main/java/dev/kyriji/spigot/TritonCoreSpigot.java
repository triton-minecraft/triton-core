package dev.kyriji.spigot;

import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCoreSpigot extends JavaPlugin {

	@Override
	public void onEnable() {
		System.out.println("Hello world!");

		CommandManager.registerCommands(tritonCommand -> {
			PluginCommand spigotCommand = this.getCommand(tritonCommand.getIdentifier());
			if(spigotCommand == null) throw new NullPointerException("Command " + tritonCommand.getIdentifier() + " failed to register");

			spigotCommand.setExecutor((sender, cmd, label, args) -> {
				SpigotPlayer player = new SpigotPlayer((Player) sender);
				tritonCommand.execute(player, args);
				return false;
			});
		});
	}
}
