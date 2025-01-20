package dev.kyriji.common.punishments.models;

import dev.kyriji.common.punishments.enums.PunishmentType;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_type", value = "basic")
public class PunishmentAction {

	private PunishmentType punishmentType;
	private String id;
	private String reason;
	private String issuer;
	private long time;

	public PunishmentAction() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public PunishmentType getPunishmentType() {
		return punishmentType;
	}

	public void setPunishmentType(PunishmentType punishmentType) {
		this.punishmentType = punishmentType;
	}


}
