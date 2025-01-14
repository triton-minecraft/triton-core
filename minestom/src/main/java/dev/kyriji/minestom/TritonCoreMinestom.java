package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.minestom.controllers.ConfigManager;
import dev.kyriji.minestom.hooks.MinestomChatHook;
import dev.kyriji.minestom.hooks.MinestomInventoryHook;
import dev.kyriji.minestom.implementation.MinestomCommandSender;
import dev.kyriji.minestom.implementation.MinestomPlayer;
import dev.kyriji.minestom.models.LuckpermsAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.nio.file.Path;
import java.util.Arrays;

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
				.withChat(new MinestomChatHook())
				.build();

		Path directory = Path.of("luckperms");

		LuckPerms luckPerms = LuckPermsMinestom.builder(directory)
				.commandRegistry(CommandRegistry.minestom())
				.configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin,
						new LuckpermsAdapter(plugin)
				)).dependencyManager(true)
				.enable();

	}


}