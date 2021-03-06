package com.aico.remote;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 * UART serial connection. Uses RXTX UART driver.
 * 
 * @author Victor Kaminskiy
 */
public final class SerialConnection implements Runnable, Connection {
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
	private InputStream inputStream = null;
	/**
	 * Output connection stream
	 */
	private WritableByteChannel writeChannel = null;
	/**
	 * Serial port
	 */
	private SerialPort port = null;

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
	private DataListener dataListener = null;
	private static boolean test = false;
	private static final boolean log=false;

	private ByteBuffer byteBuffer = ByteBuffer.allocate(256);

	/**
	 * Construct serial connection with specified properties
	 * 
	 * @param properties
	 *            - connection properties
	 */
	protected SerialConnection(String portName) {
		this.portName = portName;
	}

	@Override
    public void setDataListener(DataListener dataListener) {
		this.dataListener = dataListener;
	}

	public String toString() {
		return "Serial port:" + portName;
	}

	@Override
    public String getType() {
		return TYPE;
	}

	@Override
    public int write(byte[] buffer) throws IOException {

		return write(ByteBuffer.wrap(buffer));
	}

	@Override
    public void open() throws IOException {
		try {
			if (!test) {
				final CommPortIdentifier portId = CommPortIdentifier
						.getPortIdentifier(portName);
				if (portId == null) {
					throw new IllegalArgumentException("No such port: "
							+ portName);
				}

				port = portId.open("AICo team app", TIMEOUT_FOR_OPEN_PORT);
				if (port == null) {
					throw new IllegalArgumentException("Can't open " + portName);
				}
				port.setSerialPortParams(bitrate, dataBits, stopBits, parity);
				port.setFlowControlMode(flowControl);
				port.setDTR(dtr);
				inputStream = port.getInputStream();
				writeChannel = Channels.newChannel(port.getOutputStream());
			} else {
				inputStream = new FileInputStream("test.bin");
				writeChannel = Channels.newChannel(new FileOutputStream(
						"test.out"));
			}
			// inputStream = port.getInputStream();
			// writeChannel = Channels.newChannel(port.getOutputStream());
			final Thread thread = new Thread(this);
			thread.start();
			state = true;
		} catch (Exception e) {
			throw new IOException("Fail to open serial port " + portName, e);
		}
	}

	@Override
    public void close() throws IOException {
		if (port != null) {
			try {
				state = false;
				port.notifyOnDataAvailable(false);
				// port.removeEventListener();
				inputStream.close();
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

	@Override
    public boolean isOpen() {
		return state;
	}

	@Override
    public int write(ByteBuffer src) throws IOException {
		int length = 0;
		final StringBuffer buffer=new StringBuffer();
		for(int i=0;i<src.limit();i++){
			buffer.append(String.format("%02X ", src.get()));
		}
		if(log){
			System.out.println("<< "+buffer.toString());
		}
		src.rewind();
		length = writeChannel.write(src);
		return length;
	}

	@Override
	public void run() {
		while (state) {
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			try {
				byteBuffer.clear();
				while (inputStream.read() != 0x2A)
					;
				final int length = inputStream.read();
				int index = 0;
				while (index < length) {
					final int len = inputStream.read(byteBuffer.array(),
							byteBuffer.position(), length - index);
					if (len > 0) {
						index += len;
						byteBuffer.position(index);
					}
				}
				byteBuffer.flip();
				final StringBuffer buffer=new StringBuffer();
				for(int i=0;i<byteBuffer.limit();i++){
					buffer.append(String.format("%02X ", byteBuffer.get()));
				}
				if(log){
					System.out.println("<< "+buffer.toString());
				}
				byteBuffer.rewind();
				dataListener.dataReceived(byteBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
