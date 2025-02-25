package dev.kyriji.fabric.implementation;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.fabric.TritonCoreFabric;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class FabricPlayer extends FabricCommandSender<ServerPlayer> implements TritonPlayer {
	public ServerPlayer player;

	public FabricPlayer(ServerPlayer player) {
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
		PlayerAdapter<ServerPlayer> adapter = TritonCoreFabric.luckPerms.getPlayerAdapter(ServerPlayer.class);
		CachedPermissionData permissionData = adapter.getPermissionData(player);

		System.out.println("Checking permission: " + permission);
		System.out.println("Has permission: " + permissionData.checkPermission(permission).asBoolean());
		System.out.println("permissionData: " + permissionData);

		return permissionData.checkPermission(permission).asBoolean();
	}

	@Override
	public void disconnect(String reason) {
		player.connection.disconnect(Component.literal(reason));
	}
}
