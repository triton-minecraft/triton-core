package dev.kyriji.common.chat.utils;

public class ChatUtils {
	public static long parseDuration(String duration) {
		long time = 0;
		String[] split = duration.split(" ");
		for (String s : split) {
			if (s.endsWith("s")) {
				time += Integer.parseInt(s.replace("s", ""));
			} else if (s.endsWith("m")) {
				time += Integer.parseInt(s.replace("m", "")) * 60L;
			} else if (s.endsWith("h")) {
				time += Integer.parseInt(s.replace("h", "")) * 3600L;
			} else if (s.endsWith("d")) {
				time += Integer.parseInt(s.replace("d", "")) * 86400L;
			} else if (s.endsWith("w")) {
				time += Integer.parseInt(s.replace("w", "")) * 604800L;
			} else if (s.endsWith("mo")) {
				time += Integer.parseInt(s.replace("mo", "")) * 2592000L;
			} else if (s.endsWith("y")) {
				time += Integer.parseInt(s.replace("y", "")) * 31536000L;
			}
		}
		return time;
	}

	public static String formatDuration(long duration) {
		long seconds = duration % 60;
		long minutes = (duration / 60) % 60;
		long hours = (duration / 3600) % 24;
		long days = (duration / 86400) % 7;
		long weeks = (duration / 604800) % 4;
		long months = (duration / 2592000) % 12;
		long years = duration / 31536000;

		StringBuilder builder = new StringBuilder();
		if (years > 0) {
			builder.append(years).append("y ");
		}
		if (months > 0) {
			builder.append(months).append("mo ");
		}
		if (weeks > 0) {
			builder.append(weeks).append("w ");
		}
		if (days > 0) {
			builder.append(days).append("d ");
		}
		if (hours > 0) {
			builder.append(hours).append("h ");
		}
		if (minutes > 0) {
			builder.append(minutes).append("m ");
		}
		if (seconds > 0) {
			builder.append(seconds).append("s ");
		}
		return builder.toString().trim();
	}
}
