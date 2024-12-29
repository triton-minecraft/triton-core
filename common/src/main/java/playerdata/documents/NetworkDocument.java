package playerdata.documents;

import playerdata.model.PlayerDataInterface;

import java.util.UUID;

public class NetworkDocument implements PlayerDataInterface {
	private String uuid;
	private String name;

	public NetworkDocument(UUID uuid, String name) {
		this.uuid = uuid.toString();
		this.name = name;
	}

	public NetworkDocument() {

	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
