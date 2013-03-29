package com.aico.remote;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Communicator implements RemoteListener, Runnable {
	private static final int TROT_MIDDLE_VALUE = 260;
	private static final int MIDDLE_VALUE = 245;
	private static final int ROLL_MIDDLE_VALUE = 760;

	private static final int RANGE = 244;

	private SerialConnection connection;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(256);
	private ByteBuffer recvBuffer = ByteBuffer.allocate(256);
	private boolean started = false;
	private float startPrev = 0;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private Copter copter;
	
	public Communicator(Copter copter){
		this.copter=copter;
	}

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

	protected void notifyMovement() {
		for (ChangeListener listener : listeners) {
			listener.changed(copter);
		}
	}

	public void sendBuffer(String str) {
		System.out.println(str);
		try {
			sendBuffer.clear();
			sendBuffer.put((str + "\r\n").getBytes());

			sendBuffer.flip();
			connection.write(sendBuffer);
		} catch (Exception e1) {

		}
		// Thread.sleep(10);
	}

	@Override
	public void run() {
		try {
			recvBuffer.clear();
			connection.read(recvBuffer);
			recvBuffer.flip();
			System.out.println("recv");
			if (recvBuffer.remaining() == 58) {
				copter.setAx(recvBuffer.getShort());
				copter.setAy(recvBuffer.getShort());
				copter.setAz(recvBuffer.getShort());
				copter.setGx(recvBuffer.getShort());
				copter.setGy(recvBuffer.getShort());
				copter.setGz(recvBuffer.getShort());
				copter.setMx(recvBuffer.getShort());
				copter.setMy(recvBuffer.getShort());
				copter.setMz(recvBuffer.getShort());

				copter.setR1(recvBuffer.getShort());
				copter.setR2(recvBuffer.getShort());
				copter.setR3(recvBuffer.getShort());
				copter.setR4(recvBuffer.getShort());
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();

				copter.setTrottle(recvBuffer.getShort());
				copter.setRoll(recvBuffer.getShort());
				copter.setPitch(recvBuffer.getShort());
				copter.setYaw(recvBuffer.getShort());
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();

				recvBuffer.getShort();
				final short v2 = recvBuffer.getShort();
				final short v3 = recvBuffer.getShort();
				final short v4 = recvBuffer.getShort();

				
				notifyMovement();
			}
			// builder.append(new String(recvBuffer.array()));
			// for (byte b : recvBuffer.array()) {
			// builder.append(b);
			// builder.append(" ");
			// }
			// builder.append("\r\n");
			// System.out.println(builder);
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
		sendBuffer("setDC " + ((int) (TROT_MIDDLE_VALUE - RANGE * trottle))
				+ " " + ((int) (ROLL_MIDDLE_VALUE + RANGE * roll)) + " "
				+ ((int) (MIDDLE_VALUE - RANGE * pitch)) + " "
				+ ((int) (MIDDLE_VALUE - RANGE * yaw)));
	}

	public void start() {
		if (this.started) {
			setValues(-1, 0, 0, -0.95F);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0);
			started = false;
		} else {
			setValues(-1, 0, 0, 0.95F);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0);
			started = true;
		}
	}

	public void addMovementListener(ChangeListener listener) {
		listeners.add(listener);
	}
}
