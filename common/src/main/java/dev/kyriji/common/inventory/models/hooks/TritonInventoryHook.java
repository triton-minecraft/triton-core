package dev.kyriji.common.inventory.models.hooks;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonHook;
import dev.kyriji.common.models.TritonPlayer;

import java.util.List;
import java.util.function.Consumer;

public interface TritonInventoryHook extends TritonHook {
	void openInventory(TritonPlayer player, TritonInventory inventory);

	void updateInventory(TritonPlayer player, TritonInventory inventory);

	TritonItemStackHook<?> getItemStackHook();
}
