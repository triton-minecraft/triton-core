package dev.kyriji.common.chat.controllers;

import dev.kyriji.common.chat.hooks.TritonChatHook;

public class ChatManager {
	private final TritonChatHook hook;

	public ChatManager(TritonChatHook hook) {
		this.hook = hook;
	}

	public String formatMessage(String message) {
		return hook.formatMessage(message);
	}
}
