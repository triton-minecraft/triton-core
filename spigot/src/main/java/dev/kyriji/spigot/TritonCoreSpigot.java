package dev.kyriji.spigot;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.spigot.chat.ChatFormatter;
import dev.kyriji.spigot.controllers.ConfigManager;
import dev.kyriji.spigot.hooks.SpigotCommandHook;
import dev.kyriji.spigot.hooks.SpigotConfigHook;
import dev.kyriji.spigot.hooks.SpigotInventoryHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCoreSpigot extends JavaPlugin implements Listener {
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

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		String message = "&che&&l&lo world!";
		player.sendMessage(ChatFormatter.formatMessage(message));
	}
}
