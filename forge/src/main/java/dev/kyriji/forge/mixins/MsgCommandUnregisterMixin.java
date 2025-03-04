package dev.kyriji.forge.mixins;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.MsgCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MsgCommand.class)
public class MsgCommandUnregisterMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void onRegister(CommandDispatcher<CommandSourceStack> commandDispatcher, CallbackInfo ci) {
       ci.cancel();
    }
}
