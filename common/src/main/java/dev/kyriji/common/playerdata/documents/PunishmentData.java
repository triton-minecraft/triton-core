package dev.kyriji.common.playerdata.documents;

import dev.kyriji.common.playerdata.models.PlayerDataDocument;
import dev.kyriji.common.punishments.models.PunishmentAction;

import java.util.ArrayList;
import java.util.List;

public class PunishmentData extends PlayerDataDocument {
	private List<PunishmentAction> punishments = new ArrayList<>();

	public PunishmentData() {
		super();
	}

	public List<PunishmentAction> getPunishments() {
		return punishments;
	}

	public void setPunishments(List<PunishmentAction> punishments) {
		this.punishments = punishments;
	}

	public void addPunishment(PunishmentAction punishment) {
		punishments.add(punishment);
	}
}
