package dev.kyriji.fabric.mixins;

import dev.kyriji.fabric.hooks.FabricChatHook;
import dev.kyriji.fabric.hooks.FabricPunishmentHook;
import dev.kyriji.fabric.implementation.FabricPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(PlayerList.class)
public class ServerChatMixin {

	@Inject(
			method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onBroadcastChatMessage(PlayerChatMessage message, Predicate<ServerPlayer> predicate,
										@Nullable ServerPlayer sender, ChatType.Bound bound, CallbackInfo ci) {

		FabricPlayer fabricSender = new FabricPlayer(sender);
		if(FabricPunishmentHook.muteCallback.isMuted(fabricSender)) {
			ci.cancel();
			return;
		}

		PlayerChatMessage modifiedMessage = PlayerChatMessage.system(
			FabricChatHook.chatCallback.work(fabricSender, message.signedContent())
		);

		List<ServerPlayer> allPlayers = ((PlayerList) (Object) this).getPlayers();
		List<UUID> allowedRecipients = FabricChatHook.chatCallback.getRecipients(
				fabricSender,
				allPlayers.stream().map(ServerPlayer::getUUID).collect(Collectors.toList())
		);

		List<ServerPlayer> filteredPlayers = allPlayers.stream()
				.filter(player -> allowedRecipients.contains(player.getUUID()))
				.toList();

		for(ServerPlayer serverPlayer : filteredPlayers) {
			serverPlayer.sendSystemMessage(modifiedMessage.decoratedContent());
		}

		System.out.println(modifiedMessage.decoratedContent().getString());
		ci.cancel();
	}
}
