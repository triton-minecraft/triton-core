package dev.kyriji.velocity.hooks;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class VelocityPlayerDataHook implements TritonPlayerDataHook {
	Consumer<TritonProfile> joinCallback;
	Consumer<TritonProfile> quitCallback;

	@Override
	public List<PlayerDataType> getAutoLoadedDataTypes() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonProfile> callback) {
		joinCallback = callback;
	}

	@Override
	public void registerQuitCallback(Consumer<TritonProfile> callback) {
		quitCallback = callback;
	}

	@Subscribe
	public void onPlayerJoin(PreLoginEvent event) {
		TritonProfile profile = new TritonProfile() {
			@Override
			public UUID getUuid() {
				return event.getUniqueId();
			}

			@Override
			public String getName() {
				return event.getUsername();
			}
		};

		if(joinCallback != null) joinCallback.accept(profile);
	}

	@Subscribe
	public void onPlayerQuit(PlayerChatEvent event) {
		TritonProfile player = new TritonProfile() {
			@Override
			public UUID getUuid() {
				return event.getPlayer().getUniqueId();
			}

			@Override
			public String getName() {
				return event.getPlayer().getUsername();
			}
		};

		if(quitCallback != null) quitCallback.accept(player);
	}
}
