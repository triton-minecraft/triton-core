package dev.kyriji.common.punishments.hooks;

import dev.kyriji.common.chat.interfaces.ChatProvider;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.punishments.interfaces.MuteProvider;

import java.util.List;
import java.util.function.Consumer;

public interface TritonPunishmentHook {

	List<TritonPlayer> getOnlinePlayers();

	void registerJoinCallback(Consumer<TritonPlayer> callback);

	void registerChatCallback(MuteProvider callback);
}
