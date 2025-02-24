package dev.kyriji.fabric.hooks;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import dev.kyriji.common.models.TritonPlayer;

public class FabricInventoryHook implements TritonInventoryHook {
	@Override
	public void openInventory(TritonPlayer player, TritonInventory inventory) {

	}

	@Override
	public void closeInventory(TritonPlayer player, TritonInventory inventory) {

	}

	@Override
	public void updateInventory(TritonPlayer player, TritonInventory inventory) {

	}

	@Override
	public TritonItemStackHook<?> getItemStackHook() {
		return null;
	}
}
