package dev.kyriji.spigot.inventory;

import dev.kyriji.spigot.TritonCoreSpigot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryPanel implements InventoryHolder, Listener {

	public Player player;
	private Inventory inventory;
	private boolean closable = isClosable();

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public InventoryPanel(Player player) {
		this.player = player;
		buildInventory();

		TritonCoreSpigot.INSTANCE.getServer().getPluginManager().registerEvents(this, TritonCoreSpigot.INSTANCE);
	}

	public abstract String getName();
	public abstract int getRows();
	public abstract boolean isClosable();

	public abstract void onClick(InventoryClickEvent event);
	public abstract void onClose(InventoryCloseEvent event);

	@EventHandler
	public void onClickEvent(InventoryClickEvent event) {
		if(event.getInventory().getHolder() != this) return;
		event.setCancelled(true);

		onClick(event);
	}

	@EventHandler
	public void onCloseEvent(InventoryCloseEvent event) {
		if(event.getInventory().getHolder() != this) return;
		if(!closable) {
			if(!player.isOnline()) return;

			Bukkit.getScheduler().runTask(TritonCoreSpigot.INSTANCE, () -> {
				if(player.isOnline() && !closable) open();
			});
		}
		else onClose(event);
	}

	public void open() {
		player.openInventory(inventory);
	}

	public void close() {
		closable = true;
		player.closeInventory();

		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
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

	public void buildInventory() {
		inventory = Bukkit.createInventory(this, getSlots(getRows()),
				ChatColor.translateAlternateColorCodes('&', getName()));
	}

	private static int getSlots(int rows) {
		return Math.max(Math.min(rows, 6), 1) * 9;
	}

}
