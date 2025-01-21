package dev.kyriji.common.punishments.enums.suggestions;

import java.util.Arrays;
import java.util.List;

public enum BanReasonSuggestion {
	CHEATING("Cheating"),
	TOXICITY("Excessive Toxicity"),
	BUG_EXPLOITATION("Bug Exploitation"),
	;

	final String name;

	BanReasonSuggestion(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<String> toList(String hint) {
		List<String> names = Arrays.stream(values()).map(BanReasonSuggestion::getName).toList();
		if(hint == null || hint.isEmpty()) return names;
		return names.stream().filter(name -> name.toLowerCase().contains(hint.toLowerCase())).toList();
	}
}