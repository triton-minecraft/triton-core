package dev.kyriji.common.commands.controllers;

import dev.kyriji.common.commands.commands.PlayerDataTestCommand;
import dev.kyriji.common.commands.commands.ProxyCommand;
import dev.kyriji.common.commands.commands.TestCommand;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.model.TritonHook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandManager {
	public static List<TritonCommand> commands = new ArrayList<>();

	public static void registerCommand(TritonCommand command) {
		commands.add(command);
	}

	public static void init(TritonHook hook) {
		registerCommand(new TestCommand());
		registerCommand(new ProxyCommand());
		registerCommand(new PlayerDataTestCommand());

		commands.forEach(hook::registerCommand);
	}
}
