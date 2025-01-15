package dev.kyriji.common.commands.controllers;

import dev.kyriji.common.commands.commands.*;
import dev.kyriji.common.commands.hooks.TritonCommandHook;
import dev.kyriji.common.commands.models.TritonCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	public static List<TritonCommand> commands = new ArrayList<>();

	public TritonCommandHook hook;

	public void registerCommand(TritonCommand command) {
		commands.add(command);
		hook.registerCommand(command);
	}

	public CommandManager(TritonCommandHook hook) {
		this.hook = hook;

		registerCommand(new TestCommand());
		registerCommand(new ProxyCommand());
		registerCommand(new PlayerDataTestCommand());
		registerCommand(new InventoryTestCommand());
	}
}
