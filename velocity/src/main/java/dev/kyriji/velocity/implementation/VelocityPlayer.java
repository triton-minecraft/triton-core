package dev.kyriji.velocity.implementation;

import com.velocitypowered.api.proxy.Player;
import dev.kyriji.common.model.TritonPlayer;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityPlayer extends VelocityCommandSender implements TritonPlayer {
	public Player player;

	public VelocityPlayer(Player player) {
		super(player);

		this.player = player;
	}

	@Override
	public UUID getUuid() {
		return player.getUniqueId();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(Component.text(message));
	}
}
