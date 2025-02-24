package dev.kyriji.fabric;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.fabric.hooks.*;
import net.fabricmc.api.ModInitializer;

public class TritonCoreFabric implements ModInitializer {
    public static TritonCoreCommon core;

    @Override
    public void onInitialize() {

        core = TritonCoreCommon.builder(ServerType.MINESTOM)
                .withConfig(new FabricConfigHook())
                .withCommands(new FabricCommandHook())
                .withInventory(new FabricInventoryHook())
                .withPlayerData(new FabricPlayerDataHook())
                .withChat(new FabricChatHook())
                .withPunishments(new FabricPunishmentHook())
                .withTabList(new FabricTabListHook())
                .build();
    }
}
