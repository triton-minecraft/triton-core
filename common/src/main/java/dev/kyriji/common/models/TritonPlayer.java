package dev.kyriji.common.models;

public interface TritonPlayer extends TritonProfile {

	void sendMessage(String message);

	boolean hasPermission(String permission);
}
