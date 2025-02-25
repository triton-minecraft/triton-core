package dev.kyriji.fabric.hooks;

import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;

import java.util.List;
import java.util.function.Consumer;

public class FabricPlayerDataHook implements TritonPlayerDataHook {
	public static Consumer<TritonProfile> joinCallback;
	public static Consumer<TritonProfile> quitCallback;

	@Override
	public List<PlayerDataType> getAutoLoadedDataTypes() {
		return List.of();
	}

	@Override
	public void registerJoinCallback(Consumer<TritonProfile> callback) {
		FabricPlayerDataHook.joinCallback = callback;
	}

	@Override
	public void registerQuitCallback(Consumer<TritonProfile> callback) {
		FabricPlayerDataHook.quitCallback = callback;
	}
}
