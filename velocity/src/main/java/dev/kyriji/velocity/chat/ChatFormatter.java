package dev.kyriji.velocity.chat;

public class ChatFormatter {
	public static String formatMessage(String message) {
		String[] split = message.split("&&");
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < split.length; i++) {
			String translated = split[i].replace('&', '§');
			builder.append(translated);
			if (i != split.length - 1) {
				builder.append("&");
			}
		}

		return builder.toString();
	}
}