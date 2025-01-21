package dev.kyriji.common.punishments.enums.suggestions;

import java.util.Arrays;
import java.util.List;

public enum KickReasonSuggestion {
	INAPPROPRIATE_BEHAVIOR("Inappropriate Behavior"),
	TOXICITY("Excessive Toxicity"),
	SPAMMING("Spamming"),
	;

	final String name;

	KickReasonSuggestion(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<String> toList(String hint) {
		List<String> names = Arrays.stream(values()).map(KickReasonSuggestion::getName).toList();
		if(hint == null || hint.isEmpty()) return names;
		return names.stream().filter(name -> name.toLowerCase().contains(hint.toLowerCase())).toList();
	}}