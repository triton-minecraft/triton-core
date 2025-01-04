package dev.kyriji.minestom.hooks;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

public class MinestomInventoryHook implements TritonInventoryHook {
	private int inventoryId = -1;

	@Override
	public void openInventory(TritonPlayer player, TritonInventory tritonInventory) {
		Player minestomPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
		if(minestomPlayer == null) return;

		Inventory inventory = new Inventory(getInventoryType(tritonInventory.getRows()), Component.text(tritonInventory.getTitle()));

		TritonItemStack[] items = tritonInventory.getContents();

		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) continue;

			ItemStack item = getItemStackHook().toServerItem(items[i]);
			inventory.setItemStack(i, item);
		}

		minestomPlayer.openInventory(inventory);
		this.inventoryId = inventory.getWindowId();


		var handler = MinecraftServer.getGlobalEventHandler();

		handler.addListener(InventoryPreClickEvent.class, event -> {
			if(Objects.requireNonNull(event.getInventory()).getWindowId() != this.inventoryId) return;

			event.setCancelled(true);
			tritonInventory.onClick(new InventoryClickInfo(player, event.getSlot()));
		});

		handler.addListener(InventoryCloseEvent.class, event -> {
			if(event.getInventory().getWindowId() != this.inventoryId) return;
			tritonInventory.onClose(player);
		});
	}

	@Override
	public void updateInventory(TritonPlayer player, TritonInventory tritonInventory) {
		Player minestomPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
		if(minestomPlayer == null) return;

		AbstractInventory inventory = minestomPlayer.getOpenInventory();
		if(inventory == null || inventory.getWindowId() != this.inventoryId) return;

		TritonItemStack[] items = tritonInventory.getContents();

		for(int i = 0; i < items.length; i++) {
			ItemStack item = getItemStackHook().toServerItem(items[i]);
			inventory.setItemStack(i, item, true);
		}
	}

	@Override
	public TritonItemStackHook<ItemStack> getItemStackHook() {
		return new MinestomItemStackHook();
	}

	private InventoryType getInventoryType(int rows) {
		return switch(rows) {
			case 1 -> InventoryType.CHEST_1_ROW;
			case 2 -> InventoryType.CHEST_2_ROW;
			case 3 -> InventoryType.CHEST_3_ROW;
			case 4 -> InventoryType.CHEST_4_ROW;
			case 5 -> InventoryType.CHEST_5_ROW;
			case 6 -> InventoryType.CHEST_6_ROW;
			default -> throw new IllegalArgumentException("Invalid number of rows: " + rows);
		};
	}
}
