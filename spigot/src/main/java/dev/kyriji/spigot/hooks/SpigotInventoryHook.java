package dev.kyriji.spigot.hooks;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.spigot.inventory.InventoryPanel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SpigotInventoryHook implements TritonInventoryHook {
	public Map<Integer, InventoryPanel> panelMap = new HashMap<>();

	@Override
	public void openInventory(TritonPlayer tritonPlayer, TritonInventory inventory) {
		Player player = Bukkit.getPlayer(tritonPlayer.getUuid());

		InventoryPanel panel = new InventoryPanel(player) {

			@Override
			public String getName() {
				return inventory.getTitle();
			}

			@Override
			public int getRows() {
				return inventory.getRows();
			}

			@Override
			@EventHandler
			public void onClick(InventoryClickEvent event) {
				event.setCancelled(true);
				inventory.onClick(new InventoryClickInfo(tritonPlayer, event.getSlot()));
			}

			@Override
			@EventHandler
			public void onClose(InventoryCloseEvent event) {
				inventory.onClose(tritonPlayer);
			}
		};

		panelMap.put(inventory.hashCode(), panel);
		updateInventory(tritonPlayer, inventory);

		panel.open();
	}

	@Override
	public void updateInventory(TritonPlayer player, TritonInventory inventory) {
		InventoryPanel panel = panelMap.get(inventory.hashCode());
		if(panel == null) return;

		Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
		if(bukkitPlayer == null) return;

		ItemStack[] items = new ItemStack[panel.getContents().length];

		if(inventory.getContents().length != panel.getContents().length) {
			throw new IllegalArgumentException("Inventory contents length does not match panel contents length");
		}

		for(int i = 0; i < inventory.getContents().length; i++) {
			TritonItemStack content = inventory.getContents()[i];
			if(content == null) continue;
			ItemStack item = getItemStackHook().toServerItem(content);
			items[i] = item;
		}

		panel.setContents(items);
	}

	@Override
	public TritonItemStackHook<ItemStack> getItemStackHook() {
		return new SpigotItemStackHook();
	}
}
