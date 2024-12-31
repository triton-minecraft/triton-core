package dev.kyriji.minestom.hooks;

import dev.kyriji.common.inventory.enums.TritonMaterial;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.models.hooks.TritonItemStackHook;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class MinestomItemStackHook implements TritonItemStackHook<ItemStack> {

	@Override
	public ItemStack toServerItem(TritonItemStack item) {
		Material material = Material.fromNamespaceId("minecraft:" + item.getMaterial().name().toLowerCase());
		if(material == null) throw new IllegalArgumentException("Invalid material: " + item.getMaterial().name());

		return ItemStack.builder(material)
				.amount(item.getAmount())
				.glowing(item.hasEnchantGlint())
				.customName(Component.text(item.getDisplayName()))
				.lore(item.getLore().stream().map(Component::text).toList().toArray(new Component[0]))
				.build();
	}

	@Override
	public TritonItemStack toCommonItem(ItemStack serverItem) {
		TritonMaterial tritonMaterial = TritonMaterial.valueOf(serverItem.material().name());
		TritonItemStack item = new TritonItemStack(tritonMaterial, serverItem.amount());

		item.setEnchantGlint(serverItem.has(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE));
		item.setDisplayName(serverItem.has(ItemComponent.CUSTOM_NAME) ? serverItem.get(ItemComponent.CUSTOM_NAME).toString() : null);

		List<Component> loreComponent = serverItem.has(ItemComponent.LORE) ? serverItem.get(ItemComponent.LORE) : null;
		List<String> lore = new ArrayList<>();

		if(loreComponent != null) loreComponent.forEach(component -> lore.add(component.toString()));
		item.setLore(lore);

		return item;
	}
}