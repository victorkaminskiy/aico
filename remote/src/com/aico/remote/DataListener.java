package com.aico.remote;

import java.nio.ByteBuffer;

public interface DataListener {
	public void dataReceived(ByteBuffer buffer);
}
