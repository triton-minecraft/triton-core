package dev.kyriji.fabric.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.kyriji.common.tab.records.TabPlayer;

import java.util.List;
import java.util.function.BiConsumer;

public class FabricTabListHook implements TritonTabListHook {
	@Override
	public void registerJoinCallback(BiConsumer<TritonPlayer, List<TritonPlayer>> callback) {

	}

	@Override
	public void sendTabListHeaderFooter(TritonPlayer player, String header, String footer) {

	}

	@Override
	public void sendExistingPlayerData(TritonPlayer player, List<TabPlayer> players) {

	}

	@Override
	public void updatePlayerPriority(TritonPlayer player, int priority) {

	}

	@Override
	public void updatePlayerDisplayName(TritonPlayer player, String displayName) {

	}
}
