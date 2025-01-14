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
import dev.kyriji.common.inventory.controllers.InventoryManager;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.objects.ApiSettings;

public class TritonCoreCommon {
	public static TritonCoreCommon INSTANCE;

	private final ConfigManager configManager;
	private final CommandManager commandManager;
	private final PlayerDataManager playerDataManager;
	private final InventoryManager inventoryManager;
	private final ChatManager chatManager;

	private TritonCoreCommon(Builder builder) {
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

		chatManager.init();

		INSTANCE = this;
	}

	public static Builder builder() {
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

	public static class Builder {
		private ConfigManager configManager;
		private CommandManager commandManager;
		private PlayerDataManager playerDataManager;
		private InventoryManager inventoryManager;
		private ChatManager chatManager;

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

		public Builder withPlayerData() {
			this.playerDataManager = new PlayerDataManager();
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

		public TritonCoreCommon build() {
			return new TritonCoreCommon(this);
		}
	}
}