package dev.kyriji.common.playerdata.enums;

public enum Permission {
	STAFF("staff"),
	;

	private final String identifier;

	Permission(String identifier) {
		this.identifier = "tmc." + identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
