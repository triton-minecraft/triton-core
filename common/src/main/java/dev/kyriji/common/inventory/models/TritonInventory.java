package dev.kyriji.common.inventory.models;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonPlayer;

public abstract class TritonInventory {
	private TritonItemStack[] items;
	private final int rows;

	private final String title;

	public abstract void onClick(InventoryClickInfo clickInfo);

	public abstract void onClose(TritonPlayer player);

	public TritonInventory(int rows, String title) {
		this.rows = rows;
		this.title = title;
		this.items = new TritonItemStack[rows * 9];
	}

	public void open(TritonPlayer player) {
		TritonCoreCommon.INSTANCE.getInventoryManager().openInventory(player, this);
	}

	public void update(TritonPlayer player) {
		TritonCoreCommon.INSTANCE.getInventoryManager().updateInventory(player, this);
	}

	public TritonItemStack getItem(int slot) {
		return items[slot];
	}

	public void setItem(int slot, TritonItemStack item) {
		items[slot] = item;
	}

	public void setContents(TritonItemStack[] items) {
		this.items = items;
	}

	public TritonItemStack[] getContents() {
		return items;
	}

	public int getRows() {
		return rows;
	}

	public String getTitle() {
		return title;
	}
}
