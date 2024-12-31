package dev.kyriji.common;

import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.hooks.TritonConfigHook;
import dev.kyriji.common.inventory.controllers.InventoryManager;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;

public class TritonCoreCommon {
	public static TritonCoreCommon INSTANCE;

	private final ConfigManager configManager;
	private final CommandManager commandManager;
	private final PlayerDataManager playerDataManager;
	private final InventoryManager inventoryManager;

	private TritonCoreCommon(Builder builder) {
		this.configManager = builder.configManager;
		this.commandManager = builder.commandManager;
		this.playerDataManager = builder.playerDataManager;
		this.inventoryManager = builder.inventoryManager;

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

	public static class Builder {
		private ConfigManager configManager;
		private CommandManager commandManager;
		private PlayerDataManager playerDataManager;
		private InventoryManager inventoryManager;

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

		public TritonCoreCommon build() {
			return new TritonCoreCommon(this);
		}
	}
}