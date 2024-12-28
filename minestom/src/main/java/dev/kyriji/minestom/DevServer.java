package dev.kyriji.minestom;

import net.minestom.server.MinecraftServer;

public class DevServer {
	public static void main(String[] args) {
		// Initialize the server
		MinecraftServer minecraftServer = MinecraftServer.init();

		TritonCoreMinestom.init();
		minecraftServer.start("0.0.0.0", 25565);
	}
}
