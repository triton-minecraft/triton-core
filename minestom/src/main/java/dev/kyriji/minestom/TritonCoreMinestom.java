package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.commands.TestCommand;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonHook;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.Arrays;

public class TritonCoreMinestom {

	public static void init() {
		TritonHook hook = new TritonHook() {
			@Override
			public void registerCommand(TritonCommand command) {

				Command commandInstance = new Command(command.getIdentifier()) {{

					setDefaultExecutor((sender, context) -> {
						MinestomCommandSender minestomSender = new MinestomCommandSender(sender);

						String[] args = context.getInput().split(" ");
						command.execute(minestomSender, Arrays.copyOfRange(args, 1, args.length));
					});
				}};

				MinecraftServer.getCommandManager().register(commandInstance);
			}
		};

		TritonCoreCommon.init(hook);
	}
}