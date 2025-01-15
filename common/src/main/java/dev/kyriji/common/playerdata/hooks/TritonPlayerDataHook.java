package dev.kyriji.common.playerdata.hooks;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.List;
import java.util.function.Consumer;

public interface TritonPlayerDataHook {
	List<PlayerDataType> getAutoLoadedDataTypes();

	void registerJoinCallback(Consumer<TritonProfile> callback);

	void registerQuitCallback(Consumer<TritonProfile> callback);
}
