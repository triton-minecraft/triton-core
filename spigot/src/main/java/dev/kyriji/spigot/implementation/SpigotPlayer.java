package dev.kyriji.spigot.implementation;

import dev.kyriji.common.models.TritonPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotPlayer extends SpigotCommandSender implements TritonPlayer {
	public Player player;

	public SpigotPlayer(Player player) {
		super(player);

		this.player = player;
	}

	@Override
	public UUID getUuid() {
		return player.getUniqueId();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}

	@Override
	public void disconnect(String reason) {
		player.kickPlayer(reason);
	}
}
