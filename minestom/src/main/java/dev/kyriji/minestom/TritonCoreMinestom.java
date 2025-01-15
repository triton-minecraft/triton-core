package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.minestom.controllers.ConfigManager;
import dev.kyriji.minestom.hooks.*;
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

		TritonCoreCommon core = TritonCoreCommon.builder()
				.withConfig(new MinestomConfigHook())
				.withCommands(new MinestomCommandHook())
				.withInventory(new MinestomInventoryHook())
				.withPlayerData(new MinestomPlayerDataHook())
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