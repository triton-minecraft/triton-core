package dev.kyriji.common.punishments.enums.suggestions;

import java.util.Arrays;
import java.util.List;

public enum MuteReasonSuggestion {
	TOXICITY("Excessive Toxicity"),
	SPAMMING("Spamming"),
	INAPPROPRIATE_BEHAVIOR("Inappropriate Behavior"),
	ADVERTISING("Advertising"),
	;

	final String name;

	MuteReasonSuggestion(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<String> toList(String hint) {
		List<String> names = Arrays.stream(values()).map(MuteReasonSuggestion::getName).toList();
		if(hint == null || hint.isEmpty()) return names;
		return names.stream().filter(name -> name.toLowerCase().contains(hint.toLowerCase())).toList();
	}}