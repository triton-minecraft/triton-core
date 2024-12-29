package dev.kyriji.spigot.implementation;

import dev.kyriji.common.models.TritonCommandSender;
import org.bukkit.command.CommandSender;

public class SpigotCommandSender implements TritonCommandSender {
	public CommandSender sender;

	public SpigotCommandSender(CommandSender sender) {
		this.sender = sender;
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(message);
	}

	@Override
	public String getName() {
		return sender.getName();
	}
}
