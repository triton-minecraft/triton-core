package dev.kyriji.common.model;

public interface TritonCommandSender {
	void sendMessage(String message);

	String getName();
}
