package dev.kyriji.common.inventory.models;

import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonPlayer;

public abstract class TritonInventory {
	private TritonItemStack[] items;
	private final int rows;

	private final String title;

	public abstract void onClick(InventoryClickInfo clickInfo);

	public TritonInventory(int rows, String title) {
		this.rows = rows;
		this.title = title;
		this.items = new TritonItemStack[rows * 9];
	}

	public void open(TritonPlayer player) {
		// use the inventory hook to open the inventory
	}

	public void update(TritonPlayer player) {
		// use the inventory hook to update the inventory
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
