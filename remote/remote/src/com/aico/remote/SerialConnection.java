package com.aico.remote;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * UART serial connection. Uses RXTX UART driver.
 * 
 * @author Victor Kaminskiy
 */
public final class SerialConnection {
	public static final String PORT = "port";
	public static final String HARDWARE_CONTROL = "hwcont";
	public static final String PARITY = "parity";
	public static final String STOPBITS = "stopbits";
	public static final String DATABITS = "databits";
	public static final String BITRATE = "bitrate";

	/**
	 * Connection type
	 */
	public static final String TYPE = "Serial";
	/**
	 * Port timeout
	 */
	private static final int TIMEOUT_FOR_OPEN_PORT = 4000;
	/**
	 * Input connection stream
	 */
	private InputStream inStream = null;

	private ReadableByteChannel readChannel = null;
	/**
	 * Output connection stream
	 */
	private WritableByteChannel writeChannel = null;
	/**
	 * Serial port
	 */
	private SerialPort port = null;

	/**
	 * Blocking semaphore
	 */
	private Semaphore semaphore = new Semaphore(0);
	/**
	 * Connection state. True if connected, otherwise false.
	 */
	protected boolean state = false;

	private String portName = "/dev/ttyUSB0";
	private int bitrate = 38400;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;
	private int flowControl = SerialPort.FLOWCONTROL_NONE;
	private boolean dtr = false;

	/**
	 * Construct serial connection with specified properties
	 * 
	 * @param properties
	 *            - connection properties
	 */
	protected SerialConnection(String portName) {
		this.portName=portName;
	}

	public String toString() {
		return "Serial port:" + portName;
	}

	public String getType() {
		return TYPE;
	}

	public void write(byte[] buffer) throws IOException {

		write(ByteBuffer.wrap(buffer));
	}

	public int available() {
		int length = 0;
		try {
			if ((semaphore.tryAcquire()) && (state)) {
				length = inStream.available();
			}
		} catch (Exception ex) {
			throw new RuntimeException(
					"Fail to get avaliable info serial port", ex);
		}
		return length;
	}

	public void open() throws IOException {
		try {
			final CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(portName);
			if (portId == null) {
				throw new IllegalArgumentException("No such port: " + portName);
			}

			port = portId.open("Atmel serial plugin", TIMEOUT_FOR_OPEN_PORT);
			if (port == null) {
				throw new IllegalArgumentException("Can't open " + portName);
			}
			port.setSerialPortParams(bitrate, dataBits, stopBits, parity);
			port.setFlowControlMode(flowControl);
			port.setDTR(dtr);
			port.addEventListener(new SerialPortEventListener() {

				public void serialEvent(SerialPortEvent arg0) {
					if (SerialPortEvent.DATA_AVAILABLE == arg0.getEventType()) {
						semaphore.release();
					}
				}
			});
			port.notifyOnDataAvailable(true);
			readChannel = Channels.newChannel(port.getInputStream());
			writeChannel = Channels.newChannel(port.getOutputStream());
			state = true;
		} catch (Exception e) {
			throw new IOException("Fail to open serial port " + portName, e);
		}
	}

	public int read(ByteBuffer dst,int timeout) throws IOException {
		int length = 0;
		try {
			semaphore.tryAcquire(timeout,TimeUnit.MILLISECONDS);
			if (state) {
				length = readChannel.read(dst);
			}
		} catch (Exception e) {
		}
		return length;
	}

	public void close() throws IOException {
		if (port != null) {
			try {
				state = false;
				port.notifyOnDataAvailable(false);
				// port.removeEventListener();
				readChannel.close();
				port.getInputStream().close();
				writeChannel.close();
				port.getOutputStream().close();
				synchronized (port) {
					port.close();
				}
				port = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isOpen() {
		if (readChannel != null) {
			return readChannel.isOpen();
		}
		return false;
	}

	public int write(ByteBuffer src) throws IOException {
		int length = 0;
		length = writeChannel.write(src);
		return length;
	}
}
