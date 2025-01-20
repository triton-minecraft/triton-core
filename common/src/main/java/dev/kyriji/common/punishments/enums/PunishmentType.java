package dev.kyriji.common.punishments.enums;

public enum PunishmentType {
	BAN("Ban", "ned", true, true),
	IP_BAN("IP Ban", "ned", true, true),
	MUTE("Mute", "d", true, true),
	KICK("Kick", "ed", false, true),
	WARN("Warn", "ed", false, true),
	UNBAN("Unban", "ned", false, false),
	UNMUTE("Unmute", "d", false, false),
	;

	private final String name;
	private final String suffix;
	private final boolean requiresDuration;
	private final boolean requiresReason;

	PunishmentType(String name, String suffix, boolean requiresDuration, boolean requiresReason) {
		this.name = name;
		this.suffix = suffix;
		this.requiresDuration = requiresDuration;
		this.requiresReason = requiresReason;
	}

	public String getName() {
		return name;
	}

	public boolean requiresDuration() {
		return requiresDuration;
	}

	public String getSuffix() {
		return suffix;
	}

	public boolean requiresReason() {
		return requiresReason;
	}

}
