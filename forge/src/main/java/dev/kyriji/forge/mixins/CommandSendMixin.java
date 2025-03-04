package dev.kyriji.forge.mixins;

import com.mojang.brigadier.tree.RootCommandNode;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static dev.kyriji.common.commands.controllers.CommandManager.commands;

@Mixin(Commands.class)
public class CommandSendMixin {

	@Inject(
			method = "sendCommands",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void modifyCommandMap(ServerPlayer serverPlayer, CallbackInfo ci) {

		ForgePlayer player = new ForgePlayer(serverPlayer);
		Commands commandsInstance = (Commands) (Object) this;
		RootCommandNode<CommandSourceStack> rootCommandNode = commandsInstance.getDispatcher().getRoot();

		for(TritonCommand command : commands) {
			List<String> aliases = command.getAliases();

			if(command.getPermission() != null && !player.hasPermission(command.getPermission().getIdentifier())) {
				rootCommandNode.getChildren().removeIf(node ->{
					return node.getName().equals(command.getIdentifier()) || aliases.contains(node.getName());
				});
			}
		}
	}
}