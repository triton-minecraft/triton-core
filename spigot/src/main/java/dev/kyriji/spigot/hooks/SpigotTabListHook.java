package dev.kyriji.spigot.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;
import dev.kyriji.spigot.TritonCoreSpigot;
import dev.kyriji.spigot.implementation.SpigotPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;

public class SpigotTabListHook implements TritonTabListHook, Listener {
	private BiConsumer<TritonPlayer, List<TritonPlayer>> joinCallback;

	public SpigotTabListHook() {
		JavaPlugin plugin = TritonCoreSpigot.INSTANCE;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback) {
		this.joinCallback = callback;
	}

	@Override
	public void sendTabListHeaderFooter(TritonPlayer player, String header, String footer) {
		SpigotPlayer spigotPlayer = (SpigotPlayer) player;

		ClientboundTabListPacket packet = new ClientboundTabListPacket(Component.literal(header), Component.literal(footer));

		CraftPlayer craftPlayer = (CraftPlayer) spigotPlayer.player;
		craftPlayer.getHandle().connection.send(packet);
	}

	@Override
	public void sendExistingPlayerData(TritonPlayer player, List<TabPlayer> players) {
//		if (players.isEmpty()) return;
//
//		SpigotPlayer spigotPlayer = (SpigotPlayer) player;
//		List<ClientboundPlayerInfoUpdatePacket.Entry> entries = new ArrayList<>();
//		EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.noneOf(ClientboundPlayerInfoUpdatePacket.Action.class);
//
//		// Gather player data and prepare entries
//		players.forEach(tabPlayer -> {
//			Player bukkitPlayer = Bukkit.getPlayer(tabPlayer.uuid());
//			if (bukkitPlayer == null) return;
//
//			ServerPlayer serverPlayer = ((CraftPlayer) bukkitPlayer).getHandle();
//			System.out.println("Player: " + serverPlayer.getName() +
//					" Order: " + tabPlayer.priority() +
//					" Name: " + tabPlayer.displayName());
//
//			// Create entry for each player
//			ClientboundPlayerInfoUpdatePacket.Entry entry = new ClientboundPlayerInfoUpdatePacket.Entry(
//					tabPlayer.uuid(),
//					null,
//					true,
//					0,
//					GameType.SURVIVAL,
//					Component.literal(tabPlayer.displayName()),
//					tabPlayer.priority(),
//					null
//			);
//			entries.add(entry);
//			entries.add(entry);
//
//			actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER);
//			actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
//			});
//
//		// Now, use reflection to bypass the constructor and directly set the entries field
//		try {
//			// Create the packet using default constructor (without entries)
//			ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(actions, new ArrayList<>());
//
//			// Get the private field 'entries' from ClientboundPlayerInfoUpdatePacket
//			Field entriesField = ClientboundPlayerInfoUpdatePacket.class.getDeclaredField("entries");
//			entriesField.setAccessible(true);
//
//			// Set the entries field to our modified list
//			entriesField.set(packet, entries);
//
//			// Send the packet to the player
//			CraftPlayer craftPlayer = (CraftPlayer) spigotPlayer.player;
//
//			System.out.println("--------------------------");
//			for(int i = 0; i < packet.entries().size(); i++) {
//				ClientboundPlayerInfoUpdatePacket.Entry entry = packet.entries().get(i);
//				ClientboundPlayerInfoUpdatePacket.Action action = packet.actions().toArray(new ClientboundPlayerInfoUpdatePacket.Action[0])[i];
//
//				System.out.println("Action: " + action.toString());
//				System.out.println(entry.toString());
//			}
//			System.out.println("--------------------------");
//
//
//			craftPlayer.getHandle().connection.send(packet);
//		} catch (NoSuchFieldException | IllegalAccessException e) {
//			throw new RuntimeException("Failed to set entries field", e);
//		}
	}

	@Override
	public void updatePlayerPriority(TritonPlayer player, int priority) {
//		SpigotPlayer spigotPlayer = (SpigotPlayer) player;
//
//		ServerPlayer serverPlayer = ((CraftPlayer) spigotPlayer.player).getHandle();
//
//		ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(
//				ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER, serverPlayer);
//
//		try {
//			Field listOrderField = ClientboundPlayerInfoUpdatePacket.Entry.class.getDeclaredField("listOrder");
//			listOrderField.setAccessible(true);
//
//
//			for(ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
//				listOrderField.set(entry, priority);
//			}
//
//		} catch(NoSuchFieldException | IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//
//		Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
//			((CraftPlayer) onlinePlayer).getHandle().connection.send(packet);
//		});
	}

	@Override
	public void updatePlayerDisplayName(TritonPlayer player, String displayName) {
//		SpigotPlayer spigotPlayer = (SpigotPlayer) player;
//
//		ServerPlayer serverPlayer = ((CraftPlayer) spigotPlayer.player).getHandle();
//		ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(
//				ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER, serverPlayer);
//
//		try {
//			Field displayNameField = ClientboundPlayerInfoUpdatePacket.Entry.class.getDeclaredField("displayName");
//			displayNameField.setAccessible(true);
//
//			for(ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
//				displayNameField.set(entry, Component.literal(displayName));
//			}
//
//		} catch(NoSuchFieldException | IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//
//		Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
//			((CraftPlayer) onlinePlayer).getHandle().connection.send(packet);
//		});
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		List<TritonPlayer> onlinePlayers = new ArrayList<>();
		Bukkit.getOnlinePlayers().forEach(player -> onlinePlayers.add(new SpigotPlayer(player)));

		joinCallback.accept(new SpigotPlayer(event.getPlayer()), onlinePlayers);
	}
}
