package dev.kyriji.minestom.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.minestom.TritonCoreMinestom;
import dev.kyriji.minestom.chat.ChatFormatter;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.scoreboard.TabList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

					List<UUID> recipients = callback.getRecipients(player, new ArrayList<>(event.getRecipients().stream()
							.map(Player::getUuid)
							.toList()));

					event.getRecipients().removeIf(recipient -> !recipients.contains(recipient.getUuid()));
				}).build()
		);
	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<MinestomPlayer> players = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
				.map(MinestomPlayer::new)
				.toList();

		return List.copyOf(players);
	}
}
