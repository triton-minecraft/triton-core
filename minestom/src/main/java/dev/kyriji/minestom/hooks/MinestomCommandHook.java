package dev.kyriji.minestom.hooks;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.util.Arrays;

public class MinestomCommandHook implements TritonCommandHook {
	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getType() != CommandType.SERVER) return;

		Command commandInstance = new Command(command.getIdentifier()) {{
			setDefaultExecutor((sender, context) -> {
				MinestomCommandSender minestomSender;

				if(sender instanceof Player) minestomSender = new MinestomPlayer((Player) sender);
				else minestomSender = new MinestomCommandSender(sender);

				String[] args = context.getInput().split(" ");
				command.execute(minestomSender, Arrays.copyOfRange(args, 1, args.length));
			});
		}};

		MinecraftServer.getCommandManager().register(commandInstance);
	}
}
