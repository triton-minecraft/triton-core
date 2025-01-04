package dev.kyriji.spigot.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class ConfigManager {
	private static final String CONFIG_RESOURCE = "/config.json";
	private static JsonObject config;
	private static final Gson gson = new Gson();

	public static void init() {
		loadConfig();
	}

	private static void loadConfig() {
		try (InputStream is = ConfigManager.class.getResourceAsStream(CONFIG_RESOURCE)) {
			if (is == null) {
				throw new IllegalStateException("Config resource not found: " + CONFIG_RESOURCE);
			}

			try (JsonReader reader = new JsonReader(new InputStreamReader(is))) {
				config = gson.fromJson(reader, JsonObject.class);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config from resources", e);
		}
	}

	public static String getValue(String key) {
		if (config == null) {
			init();
		}

		if (config.has(key)) {
			return config.get(key).getAsString();
		}
		return null;
	}
}