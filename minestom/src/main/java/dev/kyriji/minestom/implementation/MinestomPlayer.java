package dev.kyriji.minestom.implementation;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.minestom.TritonCoreMinestom;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class MinestomPlayer extends MinestomCommandSender implements TritonPlayer {
	public Player player;

	public MinestomPlayer(Player player) {
		super(player);

		this.player = player;
	}

	@Override
	public UUID getUuid() {
		return player.getUuid();
	}


	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String permission) {
		QueryOptions queryOptions = QueryOptions.builder(QueryMode.NON_CONTEXTUAL).build();
		User user = TritonCoreMinestom.luckPerms.getUserManager().getUser(player.getUuid());

		if(user == null) return false;

		return user.getCachedData().getPermissionData(queryOptions).checkPermission(permission).asBoolean();
	}
}
