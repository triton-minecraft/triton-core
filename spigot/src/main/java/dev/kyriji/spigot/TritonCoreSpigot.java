package dev.kyriji.spigot;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.spigot.controllers.ConfigManager;
import dev.kyriji.spigot.hooks.*;
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
				.withChat(new SpigotChatHook())
				.withPlayerData(new SpigotPlayerDataHook())
				.build();
	}
}
