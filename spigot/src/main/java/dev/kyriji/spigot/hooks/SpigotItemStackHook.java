package dev.kyriji.spigot.hooks;

import dev.kyriji.common.inventory.enums.TritonMaterial;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotItemStackHook implements TritonItemStackHook<ItemStack> {
	@Override
	public ItemStack toServerItem(TritonItemStack item) {
		Material material = Material.getMaterial(item.getMaterial().name());
		if(material == null) throw new IllegalArgumentException("Invalid material: " + item.getMaterial().name());

		ItemStack serverItem = new ItemStack(material, item.getAmount());
		ItemMeta meta = serverItem.getItemMeta();

		if(meta == null) throw new NullPointerException("ItemMeta is null");

		if(item.hasEnchantGlint()) {
			meta.addEnchant(Enchantment.UNBREAKING, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}

		if(item.getDisplayName() != null) meta.setDisplayName(item.getDisplayName());
		if(item.getLore() != null) meta.setLore(item.getLore());

		serverItem.setItemMeta(meta);
		return serverItem;
	}

	@Override
	public TritonItemStack toCommonItem(ItemStack serverItem) {
		TritonMaterial tritonMaterial = TritonMaterial.valueOf(serverItem.getType().name());

		TritonItemStack item = new TritonItemStack(tritonMaterial, serverItem.getAmount());

		ItemMeta meta = serverItem.getItemMeta();

		if(meta == null) throw new NullPointerException("ItemMeta is null");
		item.setDisplayName(meta.getDisplayName());
		item.setLore(meta.getLore());

		if(meta.hasEnchants()) item.setEnchantGlint(true);

		return item;
	}
}
