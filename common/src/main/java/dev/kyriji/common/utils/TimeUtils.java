package dev.kyriji.common.utils;

public class TimeUtils {
	public static String toRelativeTimestamp(long timestampMillis) {
		if(timestampMillis == -1) return "never";

		long currentTime = System.currentTimeMillis();
		long diffMillis = timestampMillis - currentTime;
		boolean isFuture = diffMillis > 0;
		long absDiffMillis = Math.abs(diffMillis);

		long seconds = absDiffMillis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		long months = days / 30;
		long years = days / 365;

		if(seconds < 5) return "just now";

		if(seconds < 60) return isFuture ? "soon" : "moments ago";

		if(minutes < 60) {
			if(minutes == 1) return isFuture ? "in a minute" : "a minute ago";
			if(minutes < 5) return isFuture ? "in a few minutes" : "a few minutes ago";
			long roundedMinutes = ((minutes + 2) / 5) * 5;
			return isFuture ? "in " + roundedMinutes + " minutes" : roundedMinutes + " minutes ago";
		}

		if(hours < 24) {
			if(hours == 1) return isFuture ? "in an hour" : "an hour ago";
			return isFuture ? "in " + hours + " hours" : hours + " hours ago";
		}

		if(days < 7) {
			if(days == 1) return isFuture ? "tomorrow" : "yesterday";
			return isFuture ? "in " + days + " days" : days + " days ago";
		}

		if(days < 30) {
			long weeks = days / 7;
			if(weeks == 1) return isFuture ? "next week" : "last week";
			return isFuture ? "in " + weeks + " weeks" : weeks + " weeks ago";
		}

		String s = isFuture ? "in " + months + " months" : months + " months ago";
		if(months < 12) {
			if(months == 1) return isFuture ? "next month" : "last month";
			if(months == 0) {
				long weeks = days / 7;
				return isFuture ? "in " + weeks + " weeks" : weeks + " weeks ago";
			}
			return s;
		}

		if(years == 0) return s;
		if(years == 1) return isFuture ? "next year" : "last year";
		return isFuture ? "in " + years + " years" : years + " years ago";
	}

	public static long parseDuration(String duration) {
		long time = 0;
		String[] split = duration.split(" ");
		for (String s : split) {
			if(s.endsWith("s")) {
				time += Integer.parseInt(s.replace("s", ""));
			} else if(s.endsWith("m")) {
				time += Integer.parseInt(s.replace("m", "")) * 60L;
			} else if(s.endsWith("h")) {
				time += Integer.parseInt(s.replace("h", "")) * 3600L;
			} else if(s.endsWith("d")) {
				time += Integer.parseInt(s.replace("d", "")) * 86400L;
			} else if(s.endsWith("w")) {
				time += Integer.parseInt(s.replace("w", "")) * 604800L;
			} else if(s.endsWith("mo")) {
				time += Integer.parseInt(s.replace("mo", "")) * 2592000L;
			} else if(s.endsWith("y")) {
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
		if(years > 0) {
			builder.append(years).append("y ");
		}
		if(months > 0) {
			builder.append(months).append("mo ");
		}
		if(weeks > 0) {
			builder.append(weeks).append("w ");
		}
		if(days > 0) {
			builder.append(days).append("d ");
		}
		if(hours > 0) {
			builder.append(hours).append("h ");
		}
		if(minutes > 0) {
			builder.append(minutes).append("m ");
		}
		if(seconds > 0) {
			builder.append(seconds).append("s ");
		}
		return builder.toString().trim();
	}
}