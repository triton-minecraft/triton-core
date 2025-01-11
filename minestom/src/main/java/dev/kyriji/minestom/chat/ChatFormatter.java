package dev.kyriji.minestom.chat;

public class ChatFormatter {
	public static String formatMessage(String message) {
		String[] split = message.split("&&");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			builder.append(split[i].replaceAll("&", "§"));
			if (i != split.length - 1) builder.append("&");
		}
		return builder.toString();
	}
}
