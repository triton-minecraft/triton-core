package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.minestom.controllers.ConfigManager;
import dev.kyriji.minestom.hooks.MinestomInventoryHook;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

public class TritonCoreMinestom {

	public static void init() {
		ConfigManager.init();

		TritonConfigHook configHook = ConfigManager::getValue;

		TritonCommandHook commandHook = command -> {
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
		};

		TritonInventoryHook inventoryHook = new MinestomInventoryHook();



		TritonCoreCommon core = TritonCoreCommon.builder()
				.withConfig(configHook)
				.withCommands(commandHook)
				.withInventory(inventoryHook)
				.withPlayerData()
				.build();

	}


}