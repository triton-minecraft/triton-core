package dev.kyriji.common.models;

public interface TritonCommandSender {
	void sendMessage(String message);

	String getName();
}
