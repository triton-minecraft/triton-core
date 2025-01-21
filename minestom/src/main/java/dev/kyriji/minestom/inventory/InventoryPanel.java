package dev.kyriji.minestom.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.EventListener;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.Objects;

public abstract class InventoryPanel {

	public Player player;
	private Inventory inventory;
	private int inventoryId;
	private boolean closable = isClosable();

	private boolean reopening = false;

	private final EventListener<InventoryPreClickEvent> preClickListener;
	private final EventListener<InventoryCloseEvent> closeListener;

	public InventoryPanel(Player player) {
		this.player = player;
		buildInventory();

		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

		preClickListener = EventListener.builder(InventoryPreClickEvent.class)
				.handler(event -> {
					if(Objects.requireNonNull(event.getInventory()).getWindowId() != this.inventoryId) return;
					event.setCancelled(true);
					onClick(event);
				}).build();

		closeListener = EventListener.builder(InventoryCloseEvent.class)
				.handler(event -> {
					if(event.getInventory().getWindowId() != this.inventoryId) return;

					if(!closable && player.isOnline()) {
						if(!reopening) {
							reopening = true;
							MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
								this.inventoryId = inventory.getWindowId();
								open();
								reopening = false;
							});
						}
					} else onClose(event);
				}).build();

		handler.addListener(preClickListener);
		handler.addListener(closeListener);
	}

	public abstract String getName();
	public abstract int getRows();
	public abstract boolean isClosable();

	public abstract void onClick(InventoryPreClickEvent event);
	public abstract void onClose(InventoryCloseEvent event);

	public void open() {
		player.openInventory(inventory);
	}

	public void close() {
		closable = true;
		player.closeInventory();
		unregisterEvents();
	}

	public void updateInventory() {
		player.getInventory().update();
	}

	public void setItem(int slot, ItemStack item) {
		inventory.setItemStack(slot, item);
		updateInventory();
	}

	public ItemStack getItem(int slot) {
		return inventory.getItemStack(slot);
	}

	public void setContents(ItemStack[] items) {
		for (int i = 0; i < items.length; i++) inventory.setItemStack(i, items[i]);
		updateInventory();
	}

	public ItemStack[] getContents() {
		return inventory.getItemStacks();
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void buildInventory() {
		inventory = new Inventory(getInventoryType(getRows()), Component.text(getName()));
		inventoryId = inventory.getWindowId();
	}

	private static int getSlots(int rows) {
		return Math.max(Math.min(rows, 6), 1) * 9;
	}

	private InventoryType getInventoryType(int rows) {
		return switch (rows) {
			case 1 -> InventoryType.CHEST_1_ROW;
			case 2 -> InventoryType.CHEST_2_ROW;
			case 3 -> InventoryType.CHEST_3_ROW;
			case 4 -> InventoryType.CHEST_4_ROW;
			case 5 -> InventoryType.CHEST_5_ROW;
			case 6 -> InventoryType.CHEST_6_ROW;
			default -> throw new IllegalArgumentException("Invalid number of rows: " + rows);
		};
	}

	public void unregisterEvents() {
		GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
		handler.removeListener(preClickListener);
		handler.removeListener(closeListener);
	}
}