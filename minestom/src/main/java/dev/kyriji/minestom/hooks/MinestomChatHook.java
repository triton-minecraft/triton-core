package dev.kyriji.minestom.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.minestom.chat.ChatFormatter;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerChatEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class MinestomChatHook implements TritonChatHook {

	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {

		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

		handler.addListener(EventListener.builder(PlayerChatEvent.class)
				.handler(event -> {
					String originalMessage = event.getRawMessage();
					MinestomPlayer player = new MinestomPlayer(event.getPlayer());

					String updatedMessage = callback.work(player, originalMessage);

					Component component = Component.text(updatedMessage);
					event.setFormattedMessage(component);
				}).build()
		);

	}
}
