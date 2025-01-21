package dev.kyriji.common.punishments.enums.suggestions;

import java.util.Arrays;
import java.util.List;

public enum WarnReasonSuggestion {
	INAPPROPRIATE_BEHAVIOR("Inappropriate Behavior"),
	TOXICITY("Excessive Toxicity"),
	SPAMMING("Spamming"),
	ADVERTISEMENT("Advertising"),
	BUG_EXPLOITATION("Bug Exploitation"),
	;

	final String name;

	WarnReasonSuggestion(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<String> toList(String hint) {
		List<String> names = Arrays.stream(values()).map(WarnReasonSuggestion::getName).toList();
		if(hint == null || hint.isEmpty()) return names;
		return names.stream().filter(name -> name.toLowerCase().contains(hint.toLowerCase())).toList();
	}}