package dev.kyriji.minestom.hooks;

import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MinestomPlayerDataHook implements TritonPlayerDataHook {
	@Override
	public List<PlayerDataType> getAutoLoadedDataTypes() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonProfile> callback) {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.addListener(AsyncPlayerPreLoginEvent.class, event -> {

			TritonProfile profile = new TritonProfile() {
				@Override
				public String getName() {
					return event.getUsername();
				}

				@Override
				public UUID getUuid() {
					return event.getPlayerUuid();
				}
			};

			callback.accept(profile);
		});
	}

	@Override
	public void registerQuitCallback(Consumer<TritonProfile> callback) {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.addListener(PlayerDisconnectEvent.class, event -> {
			callback.accept(new MinestomPlayer(event.getPlayer()));
		});
	}
}
