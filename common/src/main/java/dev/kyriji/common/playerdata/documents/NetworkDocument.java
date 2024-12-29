package dev.kyriji.common.playerdata.documents;

import dev.kyriji.common.playerdata.model.PlayerDataDocument;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import java.util.UUID;

public class NetworkDocument extends PlayerDataDocument {

	private String name = null;

	public NetworkDocument() {
		super();
	}

	public NetworkDocument(UUID uuid, String name) {
		this.uuid = uuid.toString();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}