package dev.kyriji.minestom.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.minestom.chat.ChatFormatter;

public class MinestomChatHook implements TritonChatHook {
	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}
}
