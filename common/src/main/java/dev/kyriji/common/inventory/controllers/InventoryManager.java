package dev.kyriji.common.inventory.controllers;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.models.TritonPlayer;

public class InventoryManager {
	private final TritonInventoryHook hook;

	public InventoryManager(TritonInventoryHook hook) {
		this.hook = hook;
	}

	public void openInventory(TritonPlayer player, TritonInventory inventory) {
		hook.openInventory(player, inventory);
	}

	public void updateInventory(TritonPlayer player, TritonInventory inventory) {
		hook.updateInventory(player, inventory);
	}
}
