package dev.kyriji.minestom.implementation;

import dev.kyriji.common.model.TritonPlayer;
import net.minestom.server.entity.Player;

public class MinestomPlayer extends MinestomCommandSender implements TritonPlayer {
	public Player player;

	public MinestomPlayer(Player player) {
		super(player);

		this.player = player;
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
}