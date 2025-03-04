package dev.kyriji.forge.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.interfaces.MuteProvider;
import dev.kyriji.forge.TritonCoreForge;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class ForgePunishmentHook implements TritonPunishmentHook {
	public static Consumer<TritonPlayer> joinCallback;
	public static MuteProvider muteCallback;

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<ForgePlayer> fabricPlayers = new ArrayList<>();
		TritonCoreForge.server.getPlayerList().getPlayers().forEach(player -> fabricPlayers.add(new ForgePlayer(player)));

		return List.copyOf(fabricPlayers);
	}

	@Override
	public void registerJoinCallback(Consumer<TritonPlayer> callback) {
		ForgePunishmentHook.joinCallback = callback;
	}

	@Override
	public void registerChatCallback(MuteProvider callback) {
		ForgePunishmentHook.muteCallback = callback;
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ForgePlayer forgePlayer = new ForgePlayer((ServerPlayer) event.getEntity());
		ForgePunishmentHook.joinCallback.accept(forgePlayer);
	}

	@SubscribeEvent
	public static void onPlayerChat(ServerChatEvent event) {
		ForgePlayer forgePlayer = new ForgePlayer(event.getPlayer());
		if(ForgePunishmentHook.muteCallback.isMuted(forgePlayer)) event.setCanceled(true);
	}
}
