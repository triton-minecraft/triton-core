package dev.kyriji.velocity.hooks;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VelocityCommandHook implements TritonCommandHook {

	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.PROXY && command.getCommandType() != CommandType.UNIVERSAL) return;

		CommandMeta commandMeta = TritonCoreVelocity.INSTANCE.getCommandManager()
				.metaBuilder(command.getIdentifier())
				.build();

		TritonCoreVelocity.INSTANCE.getCommandManager().register(commandMeta, new SimpleCommand() {
			@Override
			public void execute(Invocation invocation) {
				VelocityCommandSender velocitySender;

				if(invocation.source() instanceof Player) {
					velocitySender = new VelocityPlayer((Player) invocation.source());
				} else {
					velocitySender = new VelocityCommandSender(invocation.source());
				}

				if(command.getExecutorType() != ExecutorType.ALL) {
					ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

					if(command.getExecutorType() == ExecutorType.PLAYER && !(invocation.source() instanceof Player)) {
						velocitySender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by a player"));
						return;
					}

					if(command.getExecutorType() == ExecutorType.CONSOLE && invocation.source() instanceof Player) {
						velocitySender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by console"));
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
	}
}
