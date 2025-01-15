package dev.kyriji.spigot.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SpigotPlayerDataHook implements TritonPlayerDataHook, Listener {
	public Consumer<TritonProfile> joinCallback;
	public Consumer<TritonProfile> quitCallback;

	public SpigotPlayerDataHook() {
		Plugin plugin = TritonCoreSpigot.INSTANCE;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public List<PlayerDataType> getAutoLoadedDataTypes() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonProfile> callback) {
		this.joinCallback = callback;
	}

	@Override
	public void registerQuitCallback(Consumer<TritonProfile> callback) {
		this.quitCallback = callback;
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent event) {
		TritonProfile profile = new TritonProfile() {
			@Override
			public UUID getUuid() {
				return event.getUniqueId();
			}

			@Override
			public String getName() {
				return event.getName();
			}
		};

		this.joinCallback.accept(profile);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		TritonPlayer player = new SpigotPlayer(event.getPlayer());
		this.quitCallback.accept(player);
	}
}
