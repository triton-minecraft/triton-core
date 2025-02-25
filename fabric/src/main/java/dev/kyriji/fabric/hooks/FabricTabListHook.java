package dev.kyriji.fabric.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;

import java.util.List;
import java.util.function.BiConsumer;

public class FabricTabListHook implements TritonTabListHook {
	public static BiConsumer<TritonPlayer, List<TritonPlayer>> joinCallback;

	@Override
	public void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback) {
		FabricTabListHook.joinCallback = callback;
	}

	@Override
	public void sendTabListHeaderFooter(TritonPlayer player, String header, String footer) {
		FabricPlayer fabricPlayer = (FabricPlayer) player;

		ClientboundTabListPacket packet = new ClientboundTabListPacket(Component.literal(header), Component.literal(footer));
		fabricPlayer.player.connection.send(packet);
	}

	@Override
	public void sendExistingPlayerData(TritonPlayer player, List<TabPlayer> players) {
		//TODO: Implement
	}

	@Override
	public void updatePlayerPriority(TritonPlayer player, int priority) {
		//TODO: Implement
	}

	@Override
	public void updatePlayerDisplayName(TritonPlayer player, String displayName) {
		//TODO: Implement
	}
}
