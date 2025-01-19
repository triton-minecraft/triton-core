package dev.kyriji.common.playerdata.documents;

import dev.kyriji.common.playerdata.model.PlayerDataDocument;
import dev.kyriji.common.playerdata.model.StaffData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkData extends PlayerDataDocument {

	private String name = null;
	private String lastPrivateMessageSender = null;
	private List<String> ignoredPlayers = new ArrayList<>();
	private StaffData staffData = new StaffData();

	public NetworkData() {
		super();
	}

	public NetworkData(UUID uuid, String name) {
		this.uuid = uuid.toString();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastPrivateMessageSender() {
		return lastPrivateMessageSender;
	}

	public void setLastPrivateMessageSender(String lastPrivateMessageSender) {
		this.lastPrivateMessageSender = lastPrivateMessageSender;
	}

	public List<String> getIgnoredPlayers() {
		return ignoredPlayers;
	}

	public void setIgnoredPlayers(List<String> ignoredPlayers) {
		this.ignoredPlayers = ignoredPlayers;
	}

	public StaffData getStaffData() {
		return staffData;
	}

	public void setStaffData(StaffData staffData) {
		this.staffData = staffData;
	}
}