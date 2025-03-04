package dev.kyriji.forge.hooks;

import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class ForgePlayerDataHook implements TritonPlayerDataHook {
	public static Consumer<TritonProfile> joinCallback;
	public static Consumer<TritonProfile> quitCallback;

	@Override
	public List<PlayerDataType> getAutoLoadedDataTypes() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonProfile> callback) {
		ForgePlayerDataHook.joinCallback = callback;
	}

	@Override
	public void registerQuitCallback(Consumer<TritonProfile> callback) {
		ForgePlayerDataHook.quitCallback = callback;
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.getEntity() instanceof ServerPlayer player) {
			ForgePlayer forgePlayer = new ForgePlayer(player);
			joinCallback.accept(forgePlayer);
		}
	}

	@SubscribeEvent
	public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.getEntity() instanceof ServerPlayer player) {
			ForgePlayer forgePlayer = new ForgePlayer(player);
			quitCallback.accept(forgePlayer);
		}
	}
}
