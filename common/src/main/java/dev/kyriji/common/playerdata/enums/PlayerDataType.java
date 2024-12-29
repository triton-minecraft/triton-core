package dev.kyriji.common.playerdata.enums;

import dev.kyriji.common.playerdata.documents.NetworkDocument;
import dev.kyriji.common.playerdata.model.PlayerDataDocument;

public enum PlayerDataType {

	NETWORK("network", NetworkDocument.class),
	;



	private final String collectionName;
	private final Class<? extends PlayerDataDocument> documentClass;

	PlayerDataType(String collectionName, Class<? extends PlayerDataDocument> documentClass) {
		this.collectionName = collectionName;
		this.documentClass = documentClass;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public Class<? extends PlayerDataDocument> getDocumentClass() {
		return documentClass;
	}

	public static PlayerDataType fromClass(Class<? extends PlayerDataDocument> clazz) {
		for(PlayerDataType type : values()) {
			if(type.getDocumentClass().equals(clazz)) {
				return type;
			}
		}
		return null;
	}
}
