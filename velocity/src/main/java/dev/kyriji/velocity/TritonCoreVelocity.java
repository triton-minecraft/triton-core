package dev.kyriji.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.velocity.controllers.ConfigManager;
import dev.kyriji.velocity.hooks.VelocityCommandHook;
import dev.kyriji.velocity.hooks.VelocityConfigHook;
import dev.kyriji.velocity.hooks.VelocityPlayerDataHook;

import java.util.logging.Logger;

@Plugin(id = "triton-core", name = "TritonCore", version = "1.0",
		url = "https://github.com/triton-minecraft/triton-core", description = "A Core system for the TritonMC Network", authors = {"wiji, Kyro"})
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

		VelocityPlayerDataHook playerData = new VelocityPlayerDataHook();
		INSTANCE.getEventManager().register(this, playerData);

		TritonCoreCommon core = TritonCoreCommon.builder()
				.withConfig(new VelocityConfigHook())
				.withCommands(new VelocityCommandHook())
				 .withPlayerData(playerData)
				.build();

	}
}