package dev.kyriji.velocity.implementation;

import com.velocitypowered.api.proxy.Player;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.models.TritonPlayer;
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

	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}

	@Override
	public void disconnect(String reason) {
		ChatManager manager = TritonCoreCommon.INSTANCE.getChatManager();
		player.disconnect(Component.text(manager.formatMessage(reason)));
	}
}
