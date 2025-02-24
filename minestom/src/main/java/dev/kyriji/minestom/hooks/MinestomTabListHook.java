package dev.kyriji.minestom.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;

public class MinestomTabListHook implements TritonTabListHook {
	@Override
	public void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback) {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.addListener(PlayerSpawnEvent.class, event ->{
			List<TritonPlayer> players = new ArrayList<>();
			MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(minestomPlayer -> {
				players.add(new MinestomPlayer(minestomPlayer));
			});

			MinestomPlayer player = new MinestomPlayer(event.getPlayer());
			callback.accept(player, players);
		});
	}

	@Override
	public void sendTabListHeaderFooter(TritonPlayer player, String header, String footer) {
		MinestomPlayer minestomPlayer = (MinestomPlayer) player;
		minestomPlayer.player.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
	}

	@Override
	public void sendExistingPlayerData(TritonPlayer player, List<TabPlayer> players) {
		MinestomPlayer minestomPlayer = (MinestomPlayer) player;
		if(players.isEmpty()) return;

		List<PlayerInfoUpdatePacket.Entry> entries = new ArrayList<>();
		EnumSet<PlayerInfoUpdatePacket.Action> orderActions = EnumSet.noneOf(PlayerInfoUpdatePacket.Action.class);
		EnumSet<PlayerInfoUpdatePacket.Action> nameActions = EnumSet.noneOf(PlayerInfoUpdatePacket.Action.class);
		for(TabPlayer tabPlayer : players) {
			String displayName = tabPlayer.displayName().replace("&", "§");

			entries.add(new PlayerInfoUpdatePacket.Entry(
					tabPlayer.uuid(),
					tabPlayer.displayName(),
					List.of(),
					true,
					0,
					GameMode.SURVIVAL,
					Component.text(displayName),
					null,
					tabPlayer.priority())
			);

			orderActions.add(PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER);
			nameActions.add(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
		}

		PlayerInfoUpdatePacket orderPacket = new PlayerInfoUpdatePacket(orderActions, entries);
		PlayerInfoUpdatePacket namePacket = new PlayerInfoUpdatePacket(nameActions, entries);

		minestomPlayer.player.getPlayerConnection().sendPacket(orderPacket);
		minestomPlayer.player.getPlayerConnection().sendPacket(namePacket);
	}

	@Override
	public void updatePlayerPriority(TritonPlayer player, int priority) {
		PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
				player.getUuid(),
				player.getName(),
				List.of(),
				true,
				0,
				GameMode.SURVIVAL,
				Component.text(player.getName().replace("&", "§")),
				null,
				priority);

		PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER, entry);

		MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(minestomPlayer -> {
			minestomPlayer.getPlayerConnection().sendPacket(packet);
		});
	}

	@Override
	public void updatePlayerDisplayName(TritonPlayer player, String displayName) {
		PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
				player.getUuid(),
				player.getName(),
				List.of(),
				true,
				0,
				GameMode.SURVIVAL,
				Component.text(displayName.replace("&", "§")),
				null,
				0);

		PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, entry);

		MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(minestomPlayer -> {
			minestomPlayer.getPlayerConnection().sendPacket(packet);
		});
	}
}
