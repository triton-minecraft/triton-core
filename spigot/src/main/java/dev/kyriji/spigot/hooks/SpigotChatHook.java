package dev.kyriji.spigot.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.chat.ChatFormatter;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpigotChatHook implements TritonChatHook, Listener {
	ChatProvider chatCallback;

	public SpigotChatHook() {
		JavaPlugin plugin = TritonCoreSpigot.INSTANCE;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {
		this.chatCallback = callback;
	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<SpigotPlayer> players = Bukkit.getOnlinePlayers().stream().map(SpigotPlayer::new).toList();
		return List.copyOf(players);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String originalMessage = event.getMessage();
		SpigotPlayer player = new SpigotPlayer(event.getPlayer());

		String updatedMessage = chatCallback.work(player, originalMessage);

		List<UUID> recipients = chatCallback.getRecipients(player, new ArrayList<>(event.getRecipients().stream()
				.map(Entity::getUniqueId)
				.toList()));

		event.getRecipients().removeIf(recipient -> !recipients.contains(recipient.getUniqueId()));

		event.setFormat(updatedMessage);
	}
}
