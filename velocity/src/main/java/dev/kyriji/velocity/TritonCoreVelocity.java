package dev.kyriji.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonCommandSender;
import dev.kyriji.common.model.TritonHook;
import dev.kyriji.velocity.implementation.VelocityCommandSender;
import dev.kyriji.velocity.implementation.VelocityPlayer;

import java.util.Arrays;
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
		TritonHook hook = new TritonHook() {
			@Override
			public void registerCommand(TritonCommand command) {
				if(command.getType() != CommandType.PROXY) return;

				SimpleCommand commandInstance = invocation -> {
					String[] args = invocation.arguments();
					VelocityCommandSender sender;

					if(invocation.source() instanceof Player player) sender = new VelocityPlayer(player);
					else sender = new VelocityCommandSender(invocation.source());

					command.execute(sender, args);
				};

				CommandMeta meta = INSTANCE.getCommandManager().metaBuilder(command.getIdentifier()).build();
				INSTANCE.getCommandManager().register(meta, commandInstance);
			}
		};

		TritonCoreCommon.init(hook);
	}
}