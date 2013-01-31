package com.aico.remote;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Communicator implements RemoteListener, Runnable {
	private static final int TROT_MIDDLE_VALUE = 26;
	private static final int MIDDLE_VALUE = 24;
	private static final int ROLL_MIDDLE_VALUE = 76;

	private static final int RANGE = 24;

	private SerialConnection connection;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(256);
	private ByteBuffer recvBuffer = ByteBuffer.allocate(256);
	private boolean started = false;
	private float startPrev = 0;

	public void connect(String portName) {
		try {
			connection = new SerialConnection(portName);
			connection.open();
			System.out.println("con");
			final Thread thread = new Thread(this);
			thread.start();
			sendBuffer("help");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendBuffer(String str) {
		System.out.println(str);
		try {
			sendBuffer.clear();
			sendBuffer.put((str + "\r\n").getBytes());

			sendBuffer.flip();
			final int len = connection.write(sendBuffer);
			System.out.println("len= " + len);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// Thread.sleep(10);
	}

	@Override
	public void run() {
		final StringBuilder builder = new StringBuilder();
		try {
			recvBuffer.clear();
			connection.read(recvBuffer);
			recvBuffer.flip();
			builder.append(new String(recvBuffer.array()));
			// for (byte b : recvBuffer.array()) {
			// builder.append(b);
			// builder.append(" ");
			// }
			builder.append("\r\n");
			System.out.println(builder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void changed(float trottle, float roll, float pitch, float yaw,
			float start) {
		if ((start == 1F) && (startPrev != start)) {
			start();

		} else {
			setValues(trottle, roll, pitch, yaw);
		}
	}

	public void setValues(float trottle, float roll, float pitch, float yaw) {
		sendBuffer("setDC " + ((int) (TROT_MIDDLE_VALUE - RANGE * trottle)) + " "
				+ ((int) (ROLL_MIDDLE_VALUE + RANGE * roll)) + " "
				+ ((int) (MIDDLE_VALUE - RANGE * pitch)) + " "
				+ ((int) (MIDDLE_VALUE - RANGE * yaw)));
	}

	public void start() {
		if (this.started) {
			setValues(-1, 0, 0, -1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0);
			started = false;
		} else {
			setValues(-1, 0, 0, 1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0);
			started = true;
		}
	}
}
