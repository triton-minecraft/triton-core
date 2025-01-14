package dev.kyriji.common.models;

public interface TritonCommandSender extends TritonProfile {
	void sendMessage(String message);
}
