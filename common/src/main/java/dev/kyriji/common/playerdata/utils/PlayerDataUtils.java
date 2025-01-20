package dev.kyriji.common.playerdata.utils;

import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.UUID;

public class PlayerDataUtils {

	public static TritonProfile loadUser(String name) {
		NetworkData playerData = PlayerDataManager.getTemporaryPlayerData(name, PlayerDataType.NETWORK);
		if(playerData == null) return null;

		return getProfile(playerData);
	}

	public static TritonProfile loadUser(UUID uuid) {
		NetworkData playerData = PlayerDataManager.getTemporaryPlayerData(uuid, PlayerDataType.NETWORK);
		if(playerData == null) return null;

		return getProfile(playerData);
	}

	public static TritonProfile getProfile(NetworkData data) {
		return new TritonProfile() {
			@Override
			public UUID getUuid() {
				return UUID.fromString(data.getUuid());
			}

			@Override
			public String getName() {
				return data.getName();
			}
		};
	}
}
