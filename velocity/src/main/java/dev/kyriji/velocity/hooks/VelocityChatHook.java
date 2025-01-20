package dev.kyriji.velocity.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.velocity.TritonCoreVelocity;
import dev.kyriji.velocity.chat.ChatFormatter;
import dev.kyriji.velocity.implementation.VelocityPlayer;

import java.util.ArrayList;
import java.util.List;

public class VelocityChatHook implements TritonChatHook {
	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {

	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		List<VelocityPlayer> players = new ArrayList<>();
		TritonCoreVelocity.INSTANCE.getAllPlayers().forEach(player -> players.add(new VelocityPlayer(player)));
		return List.copyOf(players);
	}
}
