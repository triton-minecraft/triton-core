package dev.kyriji.fabric;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.fabric.hooks.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.MinecraftServer;

public class TritonCoreFabric implements ModInitializer {

    public static MinecraftServer server;

    public static TritonCoreCommon core;
    public static LuckPerms luckPerms;

    @Override
    public void onInitialize() {
        System.out.println("TritonCoreFabric initialized");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            TritonCoreFabric.server = server;
            luckPerms = LuckPermsProvider.get();
            core = TritonCoreCommon.builder(ServerType.MINESTOM)
                    .withConfig(new FabricConfigHook())
                    .withCommands(new FabricCommandHook())
                    .withInventory(new FabricInventoryHook())
                    .withPlayerData(new FabricPlayerDataHook())
                    .withChat(new FabricChatHook())
                    .withPunishments(new FabricPunishmentHook())
                    .withTabList(new FabricTabListHook())
                    .build();
        });
    }
}
