package dev.kyriji.common.chat.interfaces;

import dev.kyriji.common.models.TritonPlayer;

public interface ChatProvider {

	String work(TritonPlayer player, String message);
}
