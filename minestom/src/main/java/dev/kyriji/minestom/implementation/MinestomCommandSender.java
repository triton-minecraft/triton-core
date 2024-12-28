package dev.kyriji.minestom.implementation;

import dev.kyriji.common.model.TritonCommandSender;
import net.minestom.server.command.CommandSender;

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
	public String getName() {
		return sender.isPlayer() ? sender.asPlayer().getUsername() : "Console";
	}
}
