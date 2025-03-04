package dev.kyriji.forge.implementation;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.forge.TritonCoreForge;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.platform.PlayerAdapter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ForgePlayer extends ForgeCommandSender<ServerPlayer> implements TritonPlayer {
	public ServerPlayer player;

	public ForgePlayer(ServerPlayer player) {
		super(player);

		this.player = player;
	}

	@Override
	public UUID getUuid() {
		return player.getUUID();
	}


	@Override
	public void sendMessage(String message) {
		player.sendSystemMessage(Component.literal(message));
	}

	@Override
	public boolean hasPermission(String permission) {
		PlayerAdapter<ServerPlayer> adapter = TritonCoreForge.luckPerms.getPlayerAdapter(ServerPlayer.class);
		CachedPermissionData permissionData = adapter.getPermissionData(player);

		return permissionData.checkPermission(permission).asBoolean();
	}

	@Override
	public void disconnect(String reason) {
		player.connection.disconnect(Component.literal(reason));
	}
}
