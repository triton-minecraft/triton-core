package dev.kyriji.fabric.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;

import java.util.List;

public class FabricChatHook implements TritonChatHook {
	@Override
	public String formatMessage(String message) {
		return "";
	}

	@Override
	public void registerChatCallback(ChatProvider callback) {

	}

	@Override
	public List<TritonPlayer> getOnlinePlayers() {
		return List.of();
	}
}
