package com.aico.remote;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Communicator implements RemoteListener, DataListener {
	private static final int TROT_MIDDLE_VALUE = 260;
	private static final int MIDDLE_VALUE = 245;
	private static final int ROLL_MIDDLE_VALUE = 760;

	private static final int RANGE = 244;

	private SerialConnection connection;
	private boolean started = false;
	private float startPrev = 0;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private Copter copter;
	private ByteBuffer changeBuffer = ByteBuffer.allocate(12);

	public Communicator(Copter copter) {
		this.copter = copter;
		changeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	public void connect(String portName) {
		try {
			connection = new SerialConnection(portName);
			connection.setDataListener(this);
			connection.open();
			final ByteBuffer byteBuffer = ByteBuffer.allocate(3);
			byteBuffer.put((byte) 0x2A);
			byteBuffer.put((byte) 0x01);
			byteBuffer.put((byte) 0x01);
			byteBuffer.flip();
			sendBuffer(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println(id);
		switch (id) {
		case 2:
			System.out.println("Connection established");
			break;
		case 4: {

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
			break;
		}
		}
		System.out.println("<<");
		final StringBuilder builder = new StringBuilder();
		builder.append(new String(recvBuffer.array(), 0, recvBuffer.limit()));
		// for (byte b : recvBuffer.array()) {
		// builder.append(b);
		// builder.append(" ");
		// }
		builder.append("\r\n");
		System.out.println(builder);
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
		changeBuffer.clear();
		changeBuffer.put((byte) 0x2A);
		changeBuffer.put((byte) 0x9);
		changeBuffer.put((byte) 0x3);
		changeBuffer.putShort(((short) (TROT_MIDDLE_VALUE - RANGE * trottle)));
		changeBuffer.putShort(((short) (ROLL_MIDDLE_VALUE + RANGE * roll)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE - RANGE * pitch)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE - RANGE * yaw)));
		changeBuffer.flip();
		sendBuffer(changeBuffer);
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
