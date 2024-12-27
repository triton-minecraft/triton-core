package dev.kyriji.common.commands.controllers;

import dev.kyriji.common.commands.models.TritonCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandManager {
	public static List<TritonCommand> commands = new ArrayList<>();

	public static void registerCommands(Consumer<TritonCommand> consumer) {
		commands.forEach(consumer);
	}
}
