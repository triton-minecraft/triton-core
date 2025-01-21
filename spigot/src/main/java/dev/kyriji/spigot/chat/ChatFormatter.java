package dev.kyriji.spigot.chat;

import org.bukkit.ChatColor;

public class ChatFormatter {
	public static String formatMessage(String message) {
		String[] split = message.split("&&");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			builder.append(ChatColor.translateAlternateColorCodes('&', split[i]));
			if(i != split.length - 1) builder.append("&");
		}
		return builder.toString();
	}
}
