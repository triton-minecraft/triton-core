package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

public class DevServer {
	public static void main(String[] args) {
		// Initialization
		MinecraftServer minecraftServer = MinecraftServer.init();

		// Create the instance
		InstanceManager instanceManager = MinecraftServer.getInstanceManager();
		InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

		// Set the ChunkGenerator
		instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

		// Add an event callback to specify the spawning instance (and the spawn position)
		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
		globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
			final Player player = event.getPlayer();
			event.setSpawningInstance(instanceContainer);
			player.setRespawnPoint(new Pos(0, 42, 0));
		});

		// Start the server on port 25565
		minecraftServer.start("0.0.0.0", 25566);

		TritonCoreMinestom.init();
	}
}
