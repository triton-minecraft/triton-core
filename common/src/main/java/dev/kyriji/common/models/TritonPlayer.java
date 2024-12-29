package dev.kyriji.common.models;

import java.util.UUID;

public interface TritonPlayer {
	UUID getUuid();

	String getName();

	void sendMessage(String message);
}
