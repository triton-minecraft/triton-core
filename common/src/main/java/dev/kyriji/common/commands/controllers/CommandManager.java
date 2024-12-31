package dev.kyriji.common.commands.controllers;

import dev.kyriji.common.commands.commands.InventoryTestCommand;
import dev.kyriji.common.commands.commands.PlayerDataTestCommand;
import dev.kyriji.common.commands.commands.ProxyCommand;
import dev.kyriji.common.commands.commands.TestCommand;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	public static List<TritonCommand> commands = new ArrayList<>();

	public void registerCommand(TritonCommand command) {
		commands.add(command);
	}

	public CommandManager(TritonCommandHook hook) {
		registerCommand(new TestCommand());
		registerCommand(new ProxyCommand());
		registerCommand(new PlayerDataTestCommand());
		registerCommand(new InventoryTestCommand());

		commands.forEach(hook::registerCommand);
	}
}
