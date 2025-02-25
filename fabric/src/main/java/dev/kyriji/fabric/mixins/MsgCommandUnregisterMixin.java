package dev.kyriji.fabric.mixins;

import com.mojang.brigadier.CommandDispatcher;
import dev.kyriji.fabric.hooks.FabricPlayerDataHook;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MsgCommand.class)
public class MsgCommandUnregisterMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void onPlayerRemove(CommandDispatcher<CommandSourceStack> commandDispatcher, CallbackInfo ci) {
       ci.cancel();
    }
}
