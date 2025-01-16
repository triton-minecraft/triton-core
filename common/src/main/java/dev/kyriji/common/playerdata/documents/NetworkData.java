package dev.kyriji.common.playerdata.documents;

import dev.kyriji.common.playerdata.model.PlayerDataDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkData extends PlayerDataDocument {

	private String name = null;
	private String lastPrivateMessageSender = null;
	private List<String> ignoredPlayers = new ArrayList<>();

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
}