package com.aico.remote;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

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
	private Semaphore semaphore = new Semaphore(1);
	private volatile String sendString = null;

	public Communicator(Copter copter) {
		this.copter = copter;
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

	public synchronized void sendBuffer(String str) {
		sendString = str;
	}

	public void internalSend() {
		if (sendString != null) {
			try {
				sendBuffer.clear();
				synchronized (this) {
					System.out.println(sendString);
					sendBuffer.put((sendString + "\r\n").getBytes());
					sendString = null;
				}
				sendBuffer.flip();
				connection.write(sendBuffer);
			} catch (Exception e1) {

			}
		}
	}

	@Override
	public void run() {
		try {
			internalSend();
			recvBuffer.clear();
			connection.read(recvBuffer,10);
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

				copter.setThrottle(recvBuffer.getShort());
				copter.setRoll(recvBuffer.getShort());
				copter.setPitch(recvBuffer.getShort());
				copter.setYaw(recvBuffer.getShort());
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();
				recvBuffer.getShort();

				copter.setAngx(recvBuffer.getShort());
				copter.setAngy(recvBuffer.getShort());
				copter.setHead(recvBuffer.getShort());
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

	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
}
