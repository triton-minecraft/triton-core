package dev.kyriji.velocity.implementation;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.models.TritonCommandSender;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityCommandSender implements TritonCommandSender {
	public CommandSource sender;

	public VelocityCommandSender(CommandSource sender) {
		this.sender = sender;
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(Component.text(message));
	}

	@Override
	public UUID getUuid() {
		return sender instanceof Player player ? player.getUniqueId() : TritonCoreCommon.CONSOLE_UUID;
	}

	@Override
	public String getName() {
		return sender instanceof Player player ? player.getUsername() : "Console";
	}
}
