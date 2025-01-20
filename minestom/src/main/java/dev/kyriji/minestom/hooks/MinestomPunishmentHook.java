package dev.kyriji.minestom.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MinestomPunishmentHook implements TritonPunishmentHook {
	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<MinestomPlayer> players = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
				.map(MinestomPlayer::new)
				.toList();

		return List.copyOf(players);
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.addListener(PlayerSpawnEvent.class, event -> {
			MinestomPlayer player = new MinestomPlayer(event.getPlayer());
			callback.accept(player);
		});
	}

	@Override
	public void registerChatCallback(MuteProvider callback) {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.addListener(PlayerChatEvent.class, event -> {
			boolean isMuted = callback.isMuted(new MinestomPlayer(event.getPlayer()));
			event.setCancelled(isMuted);
		});
	};
}
