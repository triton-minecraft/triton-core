package dev.kyriji.forge;

import com.mojang.logging.LogUtils;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.forge.hooks.*;
import net.luckperms.api.LuckPerms;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TritonCoreForge.MODID)
public class TritonCoreForge {

    public static TritonCoreCommon core;
    public static LuckPerms luckPerms;

    public static MinecraftServer server;

    public static final String MODID = "tritoncoreforge";
    private static final Logger LOGGER = LogUtils.getLogger();


    public TritonCoreForge(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("TritonCoreForge Server Mod Loaded!");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting event triggered!");
        server = event.getServer();

        core = TritonCoreCommon.builder(ServerType.FORGE)
                .withConfig(new ForgeConfigHook())
                .withCommands(new ForgeCommandHook())
                .withInventory(new ForgeInventoryHook())
                .withPlayerData(new ForgePlayerDataHook())
                .withChat(new ForgeChatHook())
                .withPunishments(new ForgePunishmentHook())
                .withTabList(new ForgeTabListHook())
                .build();
    }
}
