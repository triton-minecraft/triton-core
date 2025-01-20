package dev.kyriji.minestom;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.config.documents.CoreConfig;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.minestom.controllers.ConfigManager;
import dev.kyriji.minestom.hooks.*;
import dev.kyriji.minestom.models.LuckpermsAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.extras.velocity.VelocityProxy;

import java.nio.file.Path;

public class TritonCoreMinestom {
	public static TritonCoreCommon core;
	public static LuckPerms luckPerms;

	public static void main(String[] args) {}

	public static void init() {
		ConfigManager.init();

		core = TritonCoreCommon.builder()
				.withConfig(new MinestomConfigHook())
				.withCommands(new MinestomCommandHook())
				.withInventory(new MinestomInventoryHook())
				.withPlayerData(new MinestomPlayerDataHook())
				.withChat(new MinestomChatHook())
				.withPunishments(new MinestomPunishmentHook())
				.build();

		Path directory = Path.of("luckperms");

		luckPerms = LuckPermsMinestom.builder(directory)
				.commandRegistry(CommandRegistry.minestom())
				.configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin,
						new LuckpermsAdapter(plugin)
				)).dependencyManager(true)
				.enable();

		CoreConfig coreConfig = dev.kyriji.common.config.controllers.ConfigManager.getConfig(ConfigType.CORE);
		if(coreConfig == null) throw new NullPointerException("Core config not found");

		if(System.getenv("ENV") != null && System.getenv("ENV").equals("prod"))
			VelocityProxy.enable(coreConfig.getVelocitySecret());
	}
}