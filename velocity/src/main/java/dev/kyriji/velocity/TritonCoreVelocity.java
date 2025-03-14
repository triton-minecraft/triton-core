package dev.kyriji.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.velocity.controllers.ConfigManager;
import dev.kyriji.velocity.hooks.*;

import java.util.logging.Logger;

@Plugin(
	id = "triton-core",
	name = "TritonCore",
	version = "1.0",
	url = "https://github.com/triton-minecraft/triton-core",
	description = "A Core system for the TritonMC Network",
	authors = {"wiji, Kyro"},
	dependencies = {
		@Dependency(id = "triton-dependencies")
	}
)
public class TritonCoreVelocity {

	public static ProxyServer INSTANCE;
	public final Logger logger;

	@Inject
	public TritonCoreVelocity(ProxyServer server, Logger logger) {
		INSTANCE = server;
		this.logger = logger;
	}

	@Subscribe
	public void onProxyInitialize(ProxyInitializeEvent event) {
		ConfigManager.init();

		VelocityCommandHook commandHook = new VelocityCommandHook();
		INSTANCE.getEventManager().register(this, commandHook);

		VelocityPlayerDataHook playerData = new VelocityPlayerDataHook();
		INSTANCE.getEventManager().register(this, playerData);

		VelocityPunishmentHook punishmentHook = new VelocityPunishmentHook();
		INSTANCE.getEventManager().register(this, punishmentHook);

		TritonCoreCommon core = TritonCoreCommon.builder(ServerType.VELOCITY)
				.withConfig(new VelocityConfigHook())
				.withCommands(commandHook)
				.withPlayerData(playerData)
				.withPunishments(punishmentHook)
				.withChat(new VelocityChatHook())
				.build();

	}
}