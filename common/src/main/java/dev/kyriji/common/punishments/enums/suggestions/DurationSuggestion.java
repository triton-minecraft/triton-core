package dev.kyriji.common.punishments.enums.suggestions;

import java.util.Arrays;
import java.util.List;

public enum DurationSuggestion {
	ONE_HOUR("1h", 3600000),
	ONE_DAY("1d", 86400000),
	ONE_WEEK("1w", 604800000),
	ONE_MONTH("1m", 2628000000L),
	ONE_YEAR("1y", 31536000000L),
	;

	private final String name;
	private final long duration;

	DurationSuggestion(String name, long duration) {
		this.name = name;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public static List<String> toList() {
		return Arrays.stream(values()).map(DurationSuggestion::getName).toList();
	}

	public long getDuration() {
		return duration;
	}
}
