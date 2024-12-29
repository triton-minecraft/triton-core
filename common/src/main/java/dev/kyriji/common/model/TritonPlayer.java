package dev.kyriji.common.model;

import java.util.UUID;

public interface TritonPlayer {
	UUID getUuid();

	String getName();

	void sendMessage(String message);
}
