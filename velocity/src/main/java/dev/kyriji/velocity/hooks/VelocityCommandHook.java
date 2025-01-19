package dev.kyriji.velocity.hooks;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.velocity.TritonCoreVelocity;
import dev.kyriji.velocity.implementation.VelocityCommandSender;
import dev.kyriji.velocity.implementation.VelocityPlayer;

public class VelocityCommandHook implements TritonCommandHook {
	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getType() != CommandType.SERVER) return;

		CommandMeta commandMeta = TritonCoreVelocity.INSTANCE.getCommandManager().metaBuilder(command.getIdentifier())
				.build();

		TritonCoreVelocity.INSTANCE.getCommandManager().register(commandMeta, (SimpleCommand) invocation -> {
			VelocityCommandSender velocitySender;

			if(invocation.source() instanceof Player) velocitySender = new VelocityPlayer((Player) invocation.source());
			else velocitySender = new VelocityCommandSender(invocation.source());

			command.execute(velocitySender, invocation.arguments());
		});
	};

}
