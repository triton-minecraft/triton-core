package dev.kyriji.minestom.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public class ConfigManager {
	private static final String CONFIG_FILE = "./config.json";
	private static JsonObject config;
	private static final Gson gson = new Gson();

	public static void init() {
		loadConfig();
	}

	private static void loadConfig() {
		File configFile = new File(CONFIG_FILE);

		if(!configFile.exists()) {
			System.out.println("config file not found: " + CONFIG_FILE);
			throw new RuntimeException("config file not found: " + CONFIG_FILE);
		}

		try (FileReader reader = new FileReader(configFile)) {
			config = gson.fromJson(reader, JsonObject.class);
			System.out.println("loaded config from " + CONFIG_FILE);
		} catch (IOException e) {
			throw new RuntimeException("failed to load config file: " + CONFIG_FILE, e);
		}
	}

	public static String getValue(String key) {
		if(config == null) {
			init();
		}

		if(config.has(key)) {
			return config.get(key).getAsString();
		}
		return null;
	}
}