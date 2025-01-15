package dev.kyriji.common.playerdata.documents;

import dev.kyriji.common.playerdata.model.PlayerDataDocument;

import java.util.UUID;

public class NetworkData extends PlayerDataDocument {

	private String name = null;
	private UUID lastPrivateMessageSender = null;

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

	public UUID getLastPrivateMessageSender() {
		return lastPrivateMessageSender;
	}

	public void setLastPrivateMessageSender(UUID lastPrivateMessageSender) {
		this.lastPrivateMessageSender = lastPrivateMessageSender;
	}
}