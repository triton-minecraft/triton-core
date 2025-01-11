package dev.kyriji.spigot.hooks;

import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.spigot.chat.ChatFormatter;

public class SpigotChatHook implements TritonChatHook {
	@Override
	public String formatMessage(String message) {
		return ChatFormatter.formatMessage(message);
	}
}
