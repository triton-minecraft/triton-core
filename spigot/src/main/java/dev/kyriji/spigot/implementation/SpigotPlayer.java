package dev.kyriji.spigot.implementation;

import dev.kyriji.common.model.TritonPlayer;
import org.bukkit.entity.Player;

public class SpigotPlayer implements TritonPlayer {
	public Player player;

	public SpigotPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
}
