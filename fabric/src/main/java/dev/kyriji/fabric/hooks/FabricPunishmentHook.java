package dev.kyriji.fabric.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;
import dev.kyriji.fabric.TritonCoreFabric;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FabricPunishmentHook implements TritonPunishmentHook {
	public static Consumer<TritonPlayer> joinCallback;
	public static MuteProvider muteCallback;

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<FabricPlayer> fabricPlayers = new ArrayList<>();
		TritonCoreFabric.server.getPlayerList().getPlayers().forEach(player -> fabricPlayers.add(new FabricPlayer(player)));

		return List.copyOf(fabricPlayers);
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {
		FabricPunishmentHook.joinCallback = callback;
	}

	@Override
	public void registerChatCallback(MuteProvider callback) {
		FabricPunishmentHook.muteCallback = callback;
	}
}
