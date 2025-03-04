package dev.kyriji.forge.hooks;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.forge.TritonCoreForge;
import dev.kyriji.forge.implementation.ForgeCommandSender;
import dev.kyriji.forge.implementation.ForgePlayer;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class ForgeCommandHook implements TritonCommandHook {
	@Override
	public void registerCommand(TritonCommand command) {
		if(command.getCommandType() != CommandType.SERVER && command.getCommandType() != CommandType.UNIVERSAL) return;

		List<String> identifiers = new ArrayList<>(List.of(command.getIdentifier()));
		identifiers.addAll(command.getAliases());

		CommandDispatcher<CommandSourceStack> dispatcher = TritonCoreForge.server.getCommands().getDispatcher();
		for(String identifier : identifiers) {

			Command<CommandSourceStack> noArgsCmd = commandContext -> {
				return executeCommand(commandContext, command, new String[0]);
			};

			Command<CommandSourceStack> withArgsCmd = commandContext -> {
				String argumentsString = commandContext.getArgument("args", String.class);
				String[] arguments = argumentsString.split(" ");
				return executeCommand(commandContext, command, arguments);
			};

			LiteralCommandNode<CommandSourceStack> cmd = LiteralArgumentBuilder.<CommandSourceStack> literal(identifier)
					.executes(noArgsCmd).build();

			ArgumentCommandNode<CommandSourceStack, String> args = RequiredArgumentBuilder.<CommandSourceStack, String> argument("args", greedyString())
					.suggests((commandContext, suggestionsBuilder) -> {
						String argumentsString = "";
						try {
							argumentsString = commandContext.getArgument("args", String.class);
						} catch (IllegalArgumentException ignored) {
						}
						String[] arguments = argumentsString.split(" ");
						List<String> completions = command.getTabCompletions(new ForgeCommandSender<>(commandContext.getSource()), arguments);

						completions.forEach(suggestionsBuilder::suggest);

						return suggestionsBuilder.buildFuture();
					})
					.executes(withArgsCmd)
					.build();

			cmd.addChild(args);
			dispatcher.getRoot().addChild(cmd);

			dispatcher.register(cmd.createBuilder());
		}
	}

	private int executeCommand(CommandContext<CommandSourceStack> commandContext, TritonCommand command, String[] arguments) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		ForgeCommandSender<?> sender = new ForgeCommandSender<>(commandContext.getSource());

		if(commandContext.getSource().getPlayer() != null && command.getPermission() != null) {
			ForgePlayer fabricPlayer = new ForgePlayer(commandContext.getSource().getPlayer());

			if(!fabricPlayer.hasPermission(command.getPermission().getIdentifier())) {
				fabricPlayer.sendMessage(chatManager.formatMessage("&cYou do not have permission to execute this command"));
				return 0;
			}
		}

		command.execute(sender, arguments);
		return 1;
	}

}
