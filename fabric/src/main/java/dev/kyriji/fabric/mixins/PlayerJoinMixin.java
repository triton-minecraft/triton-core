package dev.kyriji.fabric.mixins;

import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.fabric.TritonCoreFabric;
import dev.kyriji.fabric.hooks.FabricPlayerDataHook;
import dev.kyriji.fabric.hooks.FabricPunishmentHook;
import dev.kyriji.fabric.hooks.FabricTabListHook;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerList.class)
public class PlayerJoinMixin {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void onPlaceNewPlayer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        FabricPlayer fabricPlayer = new FabricPlayer(serverPlayer);
        FabricPunishmentHook.joinCallback.accept(fabricPlayer);
        FabricPlayerDataHook.joinCallback.accept(fabricPlayer);

        List<FabricPlayer> fabricPlayers = new ArrayList<>();
        TritonCoreFabric.server.getPlayerList().getPlayers().forEach(player -> fabricPlayers.add(new FabricPlayer(player)));

        FabricTabListHook.joinCallback.accept(fabricPlayer, List.copyOf(fabricPlayers));
    }
}
