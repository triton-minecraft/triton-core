package dev.kyriji.common.inventory.models.hooks;

import dev.kyriji.common.inventory.models.TritonItemStack;

public interface TritonItemStackHook<T> {
    T toServerItem(TritonItemStack item);
    TritonItemStack toCommonItem(T serverItem);
}