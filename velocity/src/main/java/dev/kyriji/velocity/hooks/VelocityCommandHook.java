package dev.kyriji.velocity.hooks;

import com.mojang.brigadier.tree.RootCommandNode;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.Player;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.velocity.TritonCoreVelocity;
import dev.kyriji.velocity.implementation.VelocityCommandSender;
import dev.kyriji.velocity.implementation.VelocityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VelocityCommandHook implements TritonCommandHook {
	public static List<TritonCommand> commands = new ArrayList<>();

	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.PROXY && command.getCommandType() != CommandType.UNIVERSAL) return;

		CommandMeta commandMeta = TritonCoreVelocity.INSTANCE.getCommandManager()
				.metaBuilder(command.getIdentifier())
				.build();

		TritonCoreVelocity.INSTANCE.getCommandManager().register(commandMeta, new SimpleCommand() {
			@Override
			public void execute(Invocation invocation) {
				ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

				VelocityCommandSender velocitySender;

				if(invocation.source() instanceof Player) {
					velocitySender = new VelocityPlayer((Player) invocation.source());
				} else {
					velocitySender = new VelocityCommandSender(invocation.source());
				}

				if(command.getExecutorType() != ExecutorType.ALL) {
					if(command.getExecutorType() == ExecutorType.PLAYER && !(invocation.source() instanceof Player)) {
						velocitySender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by a player"));
						return;
					}

					if(command.getExecutorType() == ExecutorType.CONSOLE && invocation.source() instanceof Player) {
						velocitySender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by console"));
						return;
					}
				}

				if(velocitySender instanceof VelocityPlayer player && command.getPermission() != null) {
					if(!player.hasPermission(command.getPermission().getIdentifier())) {
						player.sendMessage(chatManager.formatMessage("&cYou do not have permission to execute this command"));
						return;
					}
				}

				command.execute(velocitySender, invocation.arguments());
			}

			@Override
			public List<String> suggest(Invocation invocation) {
				VelocityCommandSender velocitySender;

				if(invocation.source() instanceof Player) {
					velocitySender = new VelocityPlayer((Player) invocation.source());
				} else {
					velocitySender = new VelocityCommandSender(invocation.source());
				}

				return command.getTabCompletions(velocitySender, invocation.arguments());
			}
		});

		commands.add(command);
	}

	@Subscribe
	public void onPlayerAvailableCommands(PlayerAvailableCommandsEvent event) {
		RootCommandNode<?> node = event.getRootNode();

		VelocityPlayer player = new VelocityPlayer(event.getPlayer());
		for(TritonCommand command : commands) {
			if(command.getPermission() != null && !player.hasPermission(command.getPermission().getIdentifier())) {
				node.removeChildByName(command.getIdentifier());
			}
		}
	}
}
