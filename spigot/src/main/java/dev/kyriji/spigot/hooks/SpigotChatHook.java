package dev.kyriji.spigot.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.chat.ChatFormatter;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String originalMessage = event.getMessage();
		SpigotPlayer player = new SpigotPlayer(event.getPlayer());

		String updatedMessage = chatCallback.work(player, originalMessage);
		event.setFormat(updatedMessage);
//		event.setMessage(updatedMessage);
	}
}
