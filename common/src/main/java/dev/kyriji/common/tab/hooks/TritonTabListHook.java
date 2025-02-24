package dev.kyriji.common.tab.hooks;

import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.records.TabPlayer;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TritonTabListHook extends TritonHook {
	void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback);

	void sendTabListHeaderFooter(TritonPlayer player, String header, String footer);

	void sendExistingPlayerData(TritonPlayer player, List<TabPlayer> players);

	void updatePlayerPriority(TritonPlayer player, int priority);

	void updatePlayerDisplayName(TritonPlayer player, String displayName);
}
