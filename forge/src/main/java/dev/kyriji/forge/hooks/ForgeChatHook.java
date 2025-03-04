package dev.kyriji.forge.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.forge.TritonCoreForge;
import dev.kyriji.forge.chat.ChatFormatter;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ForgeChatHook implements TritonChatHook {
	public static ChatProvider chatCallback;

	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {
		ForgeChatHook.chatCallback = callback;
	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<TritonPlayer> players = new ArrayList<>();
		List<ServerPlayer> serverPlayers = TritonCoreForge.server.getPlayerList().getPlayers();

		serverPlayers.forEach(player -> players.add(new ForgePlayer(player)));

		return players;
	}

	@SubscribeEvent
	public static void onPlayerChat(ServerChatEvent event) {
		ForgePlayer forgePlayer = new ForgePlayer(event.getPlayer());

		PlayerChatMessage modifiedMessage = PlayerChatMessage.system(
				chatCallback.work(forgePlayer, event.getMessage().getString())
		);

		List<ServerPlayer> allPlayers = TritonCoreForge.server.getPlayerList().getPlayers();
		List<UUID> allowedRecipients = chatCallback.getRecipients(
				forgePlayer,
				allPlayers.stream().map(ServerPlayer::getUUID).collect(Collectors.toList())
		);

		List<ServerPlayer> filteredPlayers = allPlayers.stream()
				.filter(player -> allowedRecipients.contains(player.getUUID()))
				.toList();

		for(ServerPlayer serverPlayer : filteredPlayers) {
			serverPlayer.sendSystemMessage(modifiedMessage.decoratedContent());
		}

		System.out.println(modifiedMessage.decoratedContent().getString());
		event.setCanceled(true);
	}
}
