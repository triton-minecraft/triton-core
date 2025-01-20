package dev.kyriji.spigot.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.function.Consumer;

public class SpigotPunishmentHook implements TritonPunishmentHook, Listener {
	private Consumer<TritonPlayer> joinCallback;
	private MuteProvider chatCallback;

	public SpigotPunishmentHook() {
		Plugin plugin = TritonCoreSpigot.INSTANCE;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<SpigotPlayer> players = Bukkit.getOnlinePlayers().stream().map(SpigotPlayer::new).toList();
		return List.copyOf(players);
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {
		this.joinCallback = callback;
	}

	@Override
	public void registerChatCallback(MuteProvider callback) {
		this.chatCallback = callback;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		SpigotPlayer player = new SpigotPlayer(event.getPlayer());
		if(joinCallback != null) joinCallback.accept(player);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		SpigotPlayer player = new SpigotPlayer(event.getPlayer());
		boolean isMuted = chatCallback.isMuted(player);
		event.setCancelled(isMuted);
	}
}
