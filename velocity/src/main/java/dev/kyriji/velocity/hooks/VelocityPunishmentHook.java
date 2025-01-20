package dev.kyriji.velocity.hooks;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;
import dev.kyriji.velocity.TritonCoreVelocity;
import dev.kyriji.velocity.implementation.VelocityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VelocityPunishmentHook implements TritonPunishmentHook {
	private Consumer<TritonPlayer> joinCallback;

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<TritonPlayer> players = new ArrayList<>();
		TritonCoreVelocity.INSTANCE.getAllPlayers().forEach(player -> players.add(new VelocityPlayer(player)));
		return players;
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {
		this.joinCallback = callback;
	}

	@Override
	public void registerChatCallback(MuteProvider callback) {

	}

	@Subscribe
	public void onPlayerJoin(LoginEvent event) {
		VelocityPlayer player = new VelocityPlayer(event.getPlayer());
		if(joinCallback != null) joinCallback.accept(player);
	}
}
