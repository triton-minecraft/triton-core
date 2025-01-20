package dev.kyriji.common.punishments.models;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_type", value = "timed")
public class TimedPunishmentAction extends PunishmentAction {
	private long duration;

	public TimedPunishmentAction() {

	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
