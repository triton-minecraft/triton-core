package dev.kyriji.common;

import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.chat.hooks.TritonChatHook;
import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.documents.CoreConfig;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.config.models.RedisConnection;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.common.inventory.controllers.InventoryManager;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.hooks.TritonPlayerDataHook;
import dev.kyriji.common.punishments.controllers.PunishmentManager;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.tab.controllers.TabListManager;
import dev.kyriji.common.tab.hooks.TritonTabListHook;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.objects.ApiSettings;

import java.util.UUID;

public class TritonCoreCommon {
	public static TritonCoreCommon INSTANCE;
	public static ServerType SERVER_TYPE;

	public static UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	private final ConfigManager configManager;
	private final CommandManager commandManager;
	private final PlayerDataManager playerDataManager;
	private final InventoryManager inventoryManager;
	private final ChatManager chatManager;
	private final PunishmentManager punishmentManager;
	private final TabListManager tabListManager;

	private TritonCoreCommon(Builder builder) {
		INSTANCE = this;

		ApiSettings settings = new ApiSettings();

		CoreConfig coreConfig = ConfigManager.getConfig(ConfigType.CORE);
		if(coreConfig == null) throw new NullPointerException("Core config not found");

		RedisConnection redisConnection = coreConfig.getRedisConnection();
		if(redisConnection == null) throw new NullPointerException("Redis connection not found");

		settings.setRedisHost(redisConnection.getHost());
		settings.setRedisPort(redisConnection.getPort());

		BigMinecraftAPI.init(settings);

		this.configManager = builder.configManager;
		this.commandManager = builder.commandManager;
		this.playerDataManager = builder.playerDataManager;
		this.inventoryManager = builder.inventoryManager;
		this.chatManager = builder.chatManager;
		this.punishmentManager = builder.punishmentManager;
		this.tabListManager = builder.tabListManager;

		if(chatManager != null) chatManager.init();
		if(punishmentManager != null) punishmentManager.init();
	}

	public static Builder builder(ServerType serverType) {
		TritonCoreCommon.SERVER_TYPE = serverType;
		return new Builder();
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	public PunishmentManager getPunishmentManager() {
		return punishmentManager;
	}

	public TabListManager getTabListManager() {
		return tabListManager;
	}

	public static class Builder {
		private ConfigManager configManager;
		private CommandManager commandManager;
		private PlayerDataManager playerDataManager;
		private InventoryManager inventoryManager;
		private ChatManager chatManager;
		private PunishmentManager punishmentManager;
		private TabListManager tabListManager;

		private Builder() {
		}

		public Builder withConfig(TritonConfigHook configHook) {
			this.configManager = new ConfigManager(configHook);
			return this;
		}

		public Builder withCommands(TritonCommandHook commandHook) {
			this.commandManager = new CommandManager(commandHook);
			return this;
		}

		public Builder withPlayerData(TritonPlayerDataHook playerDataHook) {
			this.playerDataManager = new PlayerDataManager(playerDataHook);
			return this;
		}

		public Builder withInventory(TritonInventoryHook inventoryHook) {
			this.inventoryManager = new InventoryManager(inventoryHook);
			return this;
		}

		public Builder withChat(TritonChatHook chatHook) {
			this.chatManager = new ChatManager(chatHook);
			return this;
		}

		public Builder withPunishments(TritonPunishmentHook punishmentHook) {
			this.punishmentManager = new PunishmentManager(punishmentHook);
			return this;
		}

		public Builder withTabList(TritonTabListHook tabListHook) {
			this.tabListManager = new TabListManager(tabListHook);
			return this;
		}

		public TritonCoreCommon build() {
			return new TritonCoreCommon(this);
		}
	}
}