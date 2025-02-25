package dev.kyriji.fabric.implementation;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.models.TritonCommandSender;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

public class FabricCommandSender<T> implements TritonCommandSender {
	public T sender;

	public FabricCommandSender(T sender) {
		this.sender = sender;

		if(!(sender instanceof CommandSourceStack) && !(sender instanceof ServerPlayer)) {
			throw new IllegalArgumentException("Invalid sender type");
		}
	}

	@Override
	public void sendMessage(String message) {
		if(sender instanceof CommandSourceStack source) {
			source.sendSystemMessage(Component.literal(message));
		} else if(sender instanceof ServerPlayer player) {
			player.sendSystemMessage(Component.literal(message));
		}
	}

	@Override
	public UUID getUuid() {
		if(sender instanceof CommandSourceStack source) {
			if(source.getEntity() != null) return source.getEntity().getUUID();
		} else if(sender instanceof ServerPlayer player) {
			return player.getUUID();
		}

		return TritonCoreCommon.CONSOLE_UUID;
	}

	@Override
	public String getName() {
		if(sender instanceof CommandSourceStack source) {
			if(source.getPlayer() != null) return source.getPlayer().getName().getString();
		} else if(sender instanceof ServerPlayer player) {
			return player.getName().getString();
		}

		return "Console";
	}
}
