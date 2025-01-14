package dev.kyriji.common.chat.hooks;

import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonHook;

public interface TritonChatHook extends TritonHook {
	String formatMessage(String message);

	void registerChatCallback(ChatProvider callback);
}
