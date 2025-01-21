package dev.kyriji.spigot.implementation;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.models.TritonCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
	public UUID getUuid() {
		return sender instanceof Player player ? player.getUniqueId() : TritonCoreCommon.CONSOLE_UUID;
	}

	@Override
	public String getName() {
		return sender.getName();
	}
}
