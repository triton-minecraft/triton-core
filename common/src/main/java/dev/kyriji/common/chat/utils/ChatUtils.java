package dev.kyriji.common.chat.utils;

import dev.wiji.bigminecraftapi.BigMinecraftAPI;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils {
	public static List<String> getOnlinePlayerNames(String hint) {
		List<String> playerNames = new ArrayList<>(BigMinecraftAPI.getNetworkManager().getPlayers().values());

		if(hint == null || hint.isEmpty()) return playerNames;

		return playerNames.stream().filter(name -> name.toLowerCase().contains(hint.toLowerCase())).toList();
	}
}
