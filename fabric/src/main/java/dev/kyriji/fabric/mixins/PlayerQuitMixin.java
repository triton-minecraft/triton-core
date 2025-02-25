package dev.kyriji.fabric.mixins;

import dev.kyriji.fabric.hooks.FabricPlayerDataHook;
import dev.kyriji.fabric.hooks.FabricPunishmentHook;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerQuitMixin {

    @Inject(method = "remove", at = @At("TAIL"))
    private void onPlayerRemove(ServerPlayer serverPlayer, CallbackInfo ci) {
        FabricPlayer fabricPlayer = new FabricPlayer(serverPlayer);

        FabricPlayerDataHook.quitCallback.accept(fabricPlayer);
    }
}
