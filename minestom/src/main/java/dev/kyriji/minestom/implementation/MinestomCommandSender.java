package dev.kyriji.minestom.implementation;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.models.TritonCommandSender;
import net.minestom.server.command.CommandSender;

import java.util.UUID;

public class MinestomCommandSender implements TritonCommandSender {
	public CommandSender sender;

	public MinestomCommandSender(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(message);
	}

	@Override
	public UUID getUuid() {
		return sender.isPlayer() ? sender.asPlayer().getUuid() : TritonCoreCommon.CONSOLE_UUID;
	}

	@Override
	public String getName() {
		return sender.isPlayer() ? sender.asPlayer().getUsername() : "Console";
	}
}
