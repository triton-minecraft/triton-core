package dev.kyriji.common.chat.interfaces;

import dev.kyriji.common.models.TritonPlayer;

import java.util.List;
import java.util.UUID;

public interface ChatProvider {

	String work(TritonPlayer player, String message);

	List<UUID> getRecipients(TritonPlayer player, List<UUID> onlinePlayers);
}
