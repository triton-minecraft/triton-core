package dev.kyriji.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.velocity.controllers.ConfigManager;
import dev.kyriji.velocity.implementation.VelocityCommandSender;
import dev.kyriji.velocity.implementation.VelocityPlayer;

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

		TritonConfigHook configHook = ConfigManager::getValue;

		TritonCommandHook commandHook = command -> {
			if(command.getType() != CommandType.SERVER) return;

			CommandMeta commandMeta = INSTANCE.getCommandManager().metaBuilder(command.getIdentifier())
					.build();

			INSTANCE.getCommandManager().register(commandMeta, (SimpleCommand) invocation -> {
				VelocityCommandSender velocitySender;

				if(invocation.source() instanceof Player) velocitySender = new VelocityPlayer((Player) invocation.source());
				else velocitySender = new VelocityCommandSender(invocation.source());

				command.execute(velocitySender, invocation.arguments());
			});
		};

		TritonCoreCommon core = TritonCoreCommon.builder()
				.withConfig(configHook)
				.withCommands(commandHook)
				.withPlayerData()
				.build();

	}
}