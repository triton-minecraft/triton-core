package dev.kyriji.spigot;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.spigot.controllers.ConfigManager;
import dev.kyriji.spigot.hooks.SpigotCommandHook;
import dev.kyriji.spigot.hooks.SpigotConfigHook;
import dev.kyriji.spigot.hooks.SpigotInventoryHook;
import dev.kyriji.spigot.implementation.SpigotCommandSender;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCoreSpigot extends JavaPlugin {
	public static TritonCoreSpigot INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;

		ConfigManager.init();

		TritonCoreCommon common = TritonCoreCommon.builder()
				.withCommands(new SpigotCommandHook())
				.withConfig(new SpigotConfigHook())
				.withInventory(new SpigotInventoryHook())
				.withPlayerData()
				.build();

	}
}
