package dev.kyriji.fabric.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.fabric.TritonCoreFabric;
import dev.kyriji.fabric.chat.ChatFormatter;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class FabricChatHook implements TritonChatHook {
	public static ChatProvider chatCallback;

	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {
		FabricChatHook.chatCallback = callback;
	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<TritonPlayer> players = new ArrayList<>();
		List<ServerPlayer> serverPlayers = TritonCoreFabric.server.getPlayerList().getPlayers();

		serverPlayers.forEach(player -> players.add(new FabricPlayer(player)));

		return players;
	}
}
