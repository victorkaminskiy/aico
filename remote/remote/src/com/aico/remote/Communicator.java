package com.aico.remote;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Communicator implements RemoteListener, DataListener, Runnable {
	private static final int TROT_MIDDLE_VALUE = 1500;
	private static final int MIDDLE_VALUE = 1500;
	private static final int ROLL_MIDDLE_VALUE = 1500;

	private static final int RANGE = 500;

	private SerialConnection connection;
	private boolean started = false;
	private float startPrev = 0;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private Copter copter;
	private ByteBuffer changeBuffer = ByteBuffer.allocate(256);

	private float trottle;
	private float roll;
	private float pitch;
	private float yaw;
	private boolean hold;
	private Semaphore received = new Semaphore(0);

	public Communicator(Copter copter) {
		this.copter = copter;
		changeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	public void connect(String portName) {
		try {
			connection = new SerialConnection(portName);
			connection.setDataListener(this);
			connection.open();
			final ByteBuffer byteBuffer = ByteBuffer.allocate(4);
			byteBuffer.put((byte) 0x2A);
			byteBuffer.put((byte) 0x02);
			byteBuffer.put((byte) 0x01);
			byteBuffer.put((byte) 0x01);
			byteBuffer.flip();
			sendBuffer(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	protected void notifyMovement() {
		for (ChangeListener listener : listeners) {
			listener.changed(copter);
		}
	}

	public synchronized void sendBuffer(ByteBuffer byteBuffer) {
		try {
			connection.write(byteBuffer);
		} catch (Exception e) {

		}
	}

	@Override
	public void dataReceived(ByteBuffer recvBuffer) {
		final int id = recvBuffer.get();
		System.out.println("recv " + id);
		switch (id) {
		case 2:
			System.out.println("Connection established");
			break;
		case 4: {
			if (recvBuffer.limit() > 10) {
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
				copter.setRC5(recvBuffer.getShort());
				copter.setRC6(recvBuffer.getShort());
				copter.setRC7(recvBuffer.getShort());
				copter.setRC8(recvBuffer.getShort());

				copter.setAngx(recvBuffer.getShort());
				copter.setAngy(recvBuffer.getShort());
				copter.setHead(recvBuffer.getShort());
				notifyMovement();
			}
			break;
		}
		}
		received.release();
	}

	@Override
	public void changed(float trottle, float roll, float pitch, float yaw,
			float start, boolean hold) {
		if ((start == 1F) && (startPrev != start)) {
			start();
		} else {
			setValues(trottle, roll, pitch, yaw, hold);
		}
	}

	public void setValues(float trottle, float roll, float pitch, float yaw,
			boolean hold) {
		this.trottle = trottle;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		this.hold = hold;
	}

	public void flush() {
		changeBuffer.clear();
		changeBuffer.put((byte) 0x2A);
		changeBuffer.put((byte) 17);
		changeBuffer.put((byte) 0x3);
		changeBuffer.putShort(((short) (TROT_MIDDLE_VALUE + RANGE * trottle)));
		changeBuffer.putShort(((short) (ROLL_MIDDLE_VALUE + RANGE * roll)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE + RANGE * pitch)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE + RANGE * yaw)));
		if (hold) {
			changeBuffer.putShort((short) 1500);
		} else {
			changeBuffer.putShort((short) 1000);
		}
		changeBuffer.putShort((short) 1000);
		changeBuffer.putShort((short) 1000);
		changeBuffer.putShort((short) 1000);
		changeBuffer.flip();
		sendBuffer(changeBuffer);
	}

	public void start() {
		if (this.started) {
			setValues(-1, 0, 0, -0.95F, false);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0, false);
			started = false;
		} else {
			setValues(-1, 0, 0, 0.95F, false);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setValues(-1, 0, 0, 0, false);
			started = true;
		}
	}

	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void run() {
		while (true) {
			try {
				received.tryAcquire(50, TimeUnit.MILLISECONDS);
				received.drainPermits();
				flush();
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
