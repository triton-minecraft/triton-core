package dev.kyriji.spigot.inventory;

import dev.kyriji.spigot.TritonCoreSpigot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class InventoryPanel implements InventoryHolder, Listener {

	public Player player;
	private Inventory inventory;
	private final int inventoryId;

	@NotNull
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public InventoryPanel(Player player) {
		this.player = player;
		this.inventoryId = Math.abs(this.hashCode());
		buildInventory();

		TritonCoreSpigot.INSTANCE.getServer().getPluginManager().registerEvents(this, TritonCoreSpigot.INSTANCE);
	}

	public abstract String getName();
	public abstract int getRows();

	@EventHandler
	public abstract void onClick(InventoryClickEvent event);

	@EventHandler
	public abstract void onClose(InventoryCloseEvent event);

	public void open() {
		player.openInventory(inventory);
	}

	public void updateInventory() {
		player.updateInventory();
	}

	public void setItem(int slot, ItemStack item) {
		inventory.setItem(slot, item);
		updateInventory();
	}

	public ItemStack getItem(int slot) {
		return inventory.getItem(slot);
	}

	public void setContents(ItemStack[] items) {
		inventory.setContents(items);
		updateInventory();
	}

	public ItemStack[] getContents() {
		return inventory.getContents();
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public void buildInventory() {
		boolean reOpen = inventory != null && Objects.requireNonNull(player).getOpenInventory().getTopInventory().getHolder() == this;

		inventory = Bukkit.createInventory(this, getSlots(getRows()),
				ChatColor.translateAlternateColorCodes('&', getName()));

		if(reOpen) player.openInventory(inventory);
	}

	private static int getSlots(int rows) {
		return Math.max(Math.min(rows, 6), 1) * 9;
	}

}
