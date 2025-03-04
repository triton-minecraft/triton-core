package dev.kyriji.forge.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;
import dev.kyriji.forge.TritonCoreForge;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class ForgeTabListHook implements TritonTabListHook {
	public static BiConsumer<TritonPlayer, List<TritonPlayer>> joinCallback;

	@Override
	public void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback) {
		joinCallback = callback;
	}

	@Override
	public void sendTabListHeaderFooter(TritonPlayer player, String header, String footer) {
		ForgePlayer fabricPlayer = (ForgePlayer) player;

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

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ForgePlayer forgePlayer = new ForgePlayer((ServerPlayer) event.getEntity());

		List<ForgePlayer> fabricPlayers = new ArrayList<>();
		TritonCoreForge.server.getPlayerList().getPlayers().forEach(player -> fabricPlayers.add(new ForgePlayer(player)));

		joinCallback.accept(forgePlayer, List.copyOf(fabricPlayers));
	}
}
