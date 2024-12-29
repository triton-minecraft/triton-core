package dev.kyriji.minestom.implementation;

import dev.kyriji.common.models.TritonPlayer;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class MinestomPlayer extends MinestomCommandSender implements TritonPlayer {
	public Player player;

	public MinestomPlayer(Player player) {
		super(player);

		this.player = player;
	}

	@Override
	public UUID getUuid() {
		return player.getUuid();
	}


	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
}
