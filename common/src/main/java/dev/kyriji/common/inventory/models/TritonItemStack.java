package dev.kyriji.common.inventory.models;

import dev.kyriji.common.inventory.enums.TritonMaterial;

import java.util.List;

public class TritonItemStack {
	private TritonMaterial tritonMaterial;
	private int amount;

	private boolean enchantGlint;
	private String displayName;
	private List<String> lore;

	public TritonItemStack(TritonMaterial tritonMaterial) {
		this(tritonMaterial, 1);
	}

	public TritonItemStack(TritonMaterial tritonMaterial, int amount) {
		this.tritonMaterial = tritonMaterial;
		this.amount = amount;
	}

	public TritonMaterial getMaterial() {
		return tritonMaterial;
	}

	public int getAmount() {
		return amount;
	}

	public boolean hasEnchantGlint() {
		return enchantGlint;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setMaterial(TritonMaterial tritonMaterial) {
		this.tritonMaterial = tritonMaterial;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setEnchantGlint(boolean enchantGlint) {
		this.enchantGlint = enchantGlint;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}
}
