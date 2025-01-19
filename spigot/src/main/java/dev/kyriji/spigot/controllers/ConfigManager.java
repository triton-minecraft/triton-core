package dev.kyriji.spigot.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class ConfigManager {
	private static final String CONFIG_FILE = "config.json";
	private static JsonObject config;
	private static final Gson gson = new Gson();
	private static JavaPlugin plugin;

	public static void init(JavaPlugin plugin) {
		ConfigManager.plugin = plugin;
		loadConfig();
	}

	private static void loadConfig() {
		File configFile = new File(plugin.getDataFolder(), CONFIG_FILE);

		if (!configFile.exists()) {
			System.out.println("config file not found: " + configFile.getPath());
			throw new RuntimeException("config file not found: " + configFile.getPath());
		}

		try (FileReader reader = new FileReader(configFile)) {
			config = gson.fromJson(reader, JsonObject.class);
			System.out.println("loaded config from " + configFile.getPath());
		} catch (IOException e) {
			throw new RuntimeException("failed to load config file: " + configFile.getPath(), e);
		}
	}

	public static String getValue(String key) {
		if (config == null) {
			init(plugin);
		}

		if (config.has(key)) {
			return config.get(key).getAsString();
		}
		return null;
	}
}