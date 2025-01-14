package dev.kyriji.minestom.hooks;

import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.models.hooks.TritonInventoryHook;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.minestom.inventory.InventoryPanel;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MinestomInventoryHook implements TritonInventoryHook {
	public Map<Integer, InventoryPanel> panelMap = new HashMap<>();

	@Override
	public void openInventory(TritonPlayer tritonPlayer, TritonInventory tritonInventory) {
		Player minestomPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(tritonPlayer.getUuid());
		if(minestomPlayer == null) return;

		InventoryPanel panel = new InventoryPanel(minestomPlayer) {
			@Override
			public String getName() {
				return tritonInventory.getTitle();
			}

			@Override
			public int getRows() {
				return tritonInventory.getRows();
			}

			@Override
			public boolean isClosable() {
				return tritonInventory.isClosable();
			}

			@Override
			public void onClick(InventoryPreClickEvent event) {
				tritonInventory.onClick(new InventoryClickInfo(tritonPlayer, event.getSlot()));
			}

			@Override
			public void onClose(InventoryCloseEvent event) {
				tritonInventory.onClose(tritonPlayer);
			}
		};

		panelMap.put(tritonInventory.hashCode(), panel);
		updateInventory(tritonPlayer, tritonInventory);

		panel.open();
	}

	@Override
	public void closeInventory(TritonPlayer player, TritonInventory tritonInventory) {
		InventoryPanel panel = panelMap.get(tritonInventory.hashCode());
		if(panel == null) return;

		Player minestomPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
		if(minestomPlayer == null) return;

		panel.close();
		panelMap.remove(tritonInventory.hashCode());
	}

	@Override
	public void updateInventory(TritonPlayer player, TritonInventory tritonInventory) {
		InventoryPanel panel = panelMap.get(tritonInventory.hashCode());
		if(panel == null) return;

		Player minestomPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
		if(minestomPlayer == null) return;

		TritonItemStack[] items = tritonInventory.getContents();

		for(int i = 0; i < items.length; i++) {
			TritonItemStack item = items[i];
			if(item == null) continue;

			panel.setItem(i, getItemStackHook().toServerItem(item));
		}
	}

	@Override
	public TritonItemStackHook<ItemStack> getItemStackHook() {
		return new MinestomItemStackHook();
	}
}
