package dev.kyriji.minestom.hooks;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.enums.ExecutorType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.util.Arrays;

public class MinestomCommandHook implements TritonCommandHook {
	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.SERVER && command.getCommandType() != CommandType.UNIVERSAL) return;

		Command commandInstance = new Command(command.getIdentifier()) {{
			setDefaultExecutor((sender, context) -> {
				ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

				MinestomCommandSender minestomSender;

				if(sender instanceof Player) minestomSender = new MinestomPlayer((Player) sender);
				else minestomSender = new MinestomCommandSender(sender);

				if(command.getExecutorType() != ExecutorType.ALL) {

					if(command.getExecutorType() == ExecutorType.PLAYER && !(sender instanceof Player)) {
						minestomSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by a player"));
						return;
					}

					if(command.getExecutorType() == ExecutorType.CONSOLE && sender instanceof Player) {
						minestomSender.sendMessage(chatManager.formatMessage("&cThis command can only be executed by console"));
						return;
					}
				}

				if(minestomSender instanceof MinestomPlayer player && command.getPermission() != null) {
					if(!player.hasPermission(command.getPermission().getIdentifier())) {
						player.sendMessage(chatManager.formatMessage("&cYou do not have permission to use this command"));
						return;
					}
				}

				String[] args = context.getInput().split(" ");
				command.execute(minestomSender, Arrays.copyOfRange(args, 1, args.length));
			});
		}};

		MinecraftServer.getCommandManager().register(commandInstance);
	}
}
