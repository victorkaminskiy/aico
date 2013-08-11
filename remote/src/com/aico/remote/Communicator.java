package com.aico.remote;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("SpellCheckingInspection")
public class Communicator implements DataListener, Runnable {
	private static final int TROT_MIDDLE_VALUE = 1500;
	private static final int MIDDLE_VALUE = 1500;
	private static final int ROLL_MIDDLE_VALUE = 1500;

	private static final int RANGE = 500;

	private Connection connection;
	private boolean started = false;
	@SuppressWarnings("UnusedDeclaration")
    private float startPrev = 0;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private Copter copter;
	private ByteBuffer changeBuffer = ByteBuffer.allocate(100);

	private float trottle = -1F;
	private float roll = 0;
	private float pitch = 0;
	private float yaw = 0;
	private int rc5 = 1000;
	private int rc6 = 0;
	private int rc7 = 0;
	private int rc8 = 0;
	private Semaphore received = new Semaphore(0);

	public Communicator(Copter copter) {
		this.copter = copter;
		changeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	public void connect(@SuppressWarnings("UnusedParameters") String portName) {
		try {
//			connection = new SerialConnection(portName);
			//connection = new UdpConnection("192.168.0.101",9930);
			connection = new UdpConnection("192.168.0.101",100);
            //connection = new UdpConnection("localhost",9930);
            connection.setDataListener(this);
			connection.open();
//			final ByteBuffer byteBuffer = ByteBuffer.allocate(4);
//			byteBuffer.put((byte) 0x2A);
//			byteBuffer.put((byte) 0x02);
//			byteBuffer.put((byte) 0x01);
//			byteBuffer.put((byte) 0x01);
//			byteBuffer.flip();
//			sendBuffer(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	public float checkRanges(float value, float min, float max) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		return value;
	}

	public void setTrottle(float trottle) {
		this.trottle = checkRanges(trottle, -1F, 1F);
	}

	public void setRoll(float roll) {
		this.roll = checkRanges(roll, -1F, 1F);
	}

	public void setPitch(float pitch) {
		this.pitch = checkRanges(pitch, -1F, 1F);
	}

	public void setYaw(float yaw) {
		this.yaw = checkRanges(yaw, -1F, 1F);
	}

	public float getTrottle() {
		return trottle;
	}

	public float getRoll() {
		return roll;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
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
			e.printStackTrace();
		}
	}

	@Override
	public void dataReceived(ByteBuffer recvBuffer) {
//		final int id = recvBuffer.get();
//		switch (id) {
//		case 2:
//			System.out.println("Connection established");
//			break;
//		case 4: {
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
				copter.setBaroPid(recvBuffer.getShort());
				copter.setAlt(recvBuffer.getShort());
				copter.setSonar(recvBuffer.getShort());
				copter.setHoldAlt(recvBuffer.getShort());

				copter.setRoll(recvBuffer.getShort());
				copter.setPitch(recvBuffer.getShort());
				copter.setYaw(recvBuffer.getShort());
				copter.setThrottle(recvBuffer.getShort());
				copter.setRC5(recvBuffer.getShort());
				copter.setRC6(recvBuffer.getShort());
				copter.setRC7(recvBuffer.getShort());
				copter.setRC8(recvBuffer.getShort());

				copter.setAngx(recvBuffer.getShort());
				copter.setAngy(recvBuffer.getShort());
				copter.setHead(recvBuffer.getShort());
				notifyMovement();
			}
//			break;
//		}
//		}
		received.release();
	}

	private int getRcValue(int val) {
		switch (val) {
		case 0:
			return 1000;
		case 1:
			return 1500;
		default:
			return 1800;
		}
	}

	public void flush() {
		changeBuffer.clear();
        //The following commented lines is for serial connection
		//changeBuffer.put((byte) 0x2A);
		//changeBuffer.put((byte) 17);
		//changeBuffer.put((byte) 0x3);
		changeBuffer.putShort(((short) (ROLL_MIDDLE_VALUE + RANGE * roll)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE + RANGE * pitch)));
		changeBuffer.putShort(((short) (MIDDLE_VALUE + RANGE * yaw)));
		changeBuffer.putShort(((short) (TROT_MIDDLE_VALUE + RANGE * trottle)));
		if (rc5 == 1000) {
			changeBuffer
					.putShort(((short) (TROT_MIDDLE_VALUE + RANGE * trottle)));
		} else {
			changeBuffer.putShort((short) rc5);
		}
		changeBuffer.putShort((short) getRcValue(rc6));
		changeBuffer.putShort((short) getRcValue(rc7));
		changeBuffer.putShort((short) rc8);
		changeBuffer.flip();
		sendBuffer(changeBuffer);
	}

	public void drop() {
		setTrottle(-1);
		setPitch(0);
		setRoll(0);
		setYaw(0);
		setRc5(1000);
		setRc6(0);
		setRc7(0);
		setRc8(1000);
	}

	public void setHoldAlt(int height) {
		setRc8(1000 + height);
	}

	public int getHoldAlt() {
		return getRc8() - 1000;
	}

	private boolean run = false;

    public void flightWithPid(final int height) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                PidRegulator.INSTANCE.clear();

                final int ALT_MIS = 2;
                int currentAlt = copter.getAlt();
                final int startAlt = currentAlt;
                arm();
                setTrottle(-0.3F);
                int stableIndex = 0;
                boolean onAlt = false;
                boolean onFlight = false;
                System.out.println("Alt dest " + currentAlt + " "
                        + (startAlt + height));
                //noinspection ConstantConditions
                while (!onAlt) {
                    final int newAlt = copter.getAlt();
                    if (newAlt < currentAlt + ALT_MIS) {
                        stableIndex++;
                    } else {
                        currentAlt = newAlt;
                        stableIndex = 0;
                        onFlight = true;
                    }
                    if (stableIndex != 0) {
                        if (!onFlight) {
                            setTrottle(getTrottle() + 0.003F);
                        } else {
                            // Old variant: setTrottle(getTrottle() + 0.0005F);
                            setTrottle((float) PidRegulator.INSTANCE.calculate(newAlt, startAlt + height));
                        }
                    }
                    System.out.println(copter.getTrottle()+" "+currentAlt+" "+onFlight+" "+stableIndex+" ["+copter.getR1()+" "+copter.getR2()+" "+copter.getR3()+" "+copter.getR4()+"]");
                }

                // TODO This function doeas not call to setHoldAlt, first of all we need to test the PID.
            }
        });
        if (!run) {
            t.start();
            run = true;
        }
    }

	public void flight(final int height) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				final int ALT_MIS = 2;
				int currentAlt = copter.getAlt();
				final int startAlt = currentAlt;
				arm();
				setTrottle(-0.3F);
				int stableIndex = 0;
				boolean onAlt = false;
				boolean onFlight = false;
				System.out.println("Alt dest " + currentAlt + " "
						+ (startAlt + height));
				while (!onAlt) {
					final int newAlt = copter.getAlt();
					if (newAlt < currentAlt + ALT_MIS) {
						stableIndex++;
					} else {
						currentAlt = newAlt;
						stableIndex = 0;
						onFlight = true;
					}
					if (stableIndex != 0) {
						if (!onFlight) {
							setTrottle(getTrottle() + 0.003F);
						} else {
							setTrottle(getTrottle() + 0.0005F);
						}
					}
					System.out.println(copter.getTrottle()+" "+currentAlt+" "+onFlight+" "+stableIndex+" ["+copter.getR1()+" "+copter.getR2()+" "+copter.getR3()+" "+copter.getR4()+"]");
					if (currentAlt > startAlt + height - 4 * ALT_MIS) {
						onAlt = true;
						setTrottle(getTrottle() - 0.07F);
						setHoldAlt(startAlt + height);
						System.out.println("Alt OK ");
					}
					if (!onAlt) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("On Air " + copter.getAlt());

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println(copter.getAlt());
				//try {
				//	Thread.sleep(4500);
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
				//System.out.println("Landing " + copter.getAlt());
				//landing();
				run = false;
			}
		});
		if (!run) {
			t.start();
			run = true;
		}
	}

	public void landing() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Landing " + copter.getAlt());
				final int ALT_MIS = 3;
				setHoldAlt(0);
				boolean landed = false;
				int currentAlt = copter.getAlt();
				int stableIndex = 0;
				while (!landed) {
					final int newAlt = copter.getAlt();
					if ((newAlt > currentAlt - ALT_MIS)
							&& (newAlt < currentAlt + ALT_MIS)) {
						stableIndex++;
					} else {
						currentAlt = newAlt;
						stableIndex = 0;
					}
					landed = stableIndex > 40;
					if (stableIndex > 8) {
						setTrottle(getTrottle() - 0.02F);
					} else {
						setTrottle(getTrottle() - 0.015F);
					}
					System.out.println("In landing " + getTrottle() + " "
							+ copter.getAlt());
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				disarm();
				System.out.println("Landed");
			}
		});
		t.start();
	}

	public void disarm() {
		drop();
		setYaw(-0.95F);
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		drop();
		started = false;
	}

	public int getRc5() {
		return rc5;
	}

	public void setRc5(int rc5) {
		if (rc5 < 1000)
			rc5 = 1000;
		this.rc5 = rc5;
	}

	@SuppressWarnings("UnusedDeclaration")
    public int getRc6() {
		return rc6;
	}

	public void setRc6(int rc6) {
		this.rc6 = rc6;
	}

	@SuppressWarnings("UnusedDeclaration")
    public int getRc7() {
		return rc7;
	}

	public void setRc7(int rc7) {
		this.rc7 = rc7;
	}

	public int getRc8() {
		return rc8;
	}

	public void setRc8(int rc8) {
		if (rc8 < 1000)
			rc8 = 1000;
		this.rc8 = rc8;
	}

	@SuppressWarnings("UnusedDeclaration")
    public boolean isStarted() {
		return started;
	}

	public void arm() {
		drop();
		setYaw(0.95F);
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		drop();
		started = true;
	}

	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
			try {
				received.tryAcquire(150, TimeUnit.MILLISECONDS);
				received.drainPermits();
				flush();
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
