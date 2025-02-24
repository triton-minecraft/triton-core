package dev.kyriji.fabric.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;

import java.util.List;
import java.util.function.Consumer;

public class FabricPunishmentHook implements TritonPunishmentHook {
	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {

	}

	@Override
	public void registerChatCallback(MuteProvider callback) {

	}
}
