package com.aico.remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * User: vk
 * Date: 7/1/13
 * Time: 12:46 AM
 */
public class UdpConnection implements Connection, Runnable {
    /**
     * Connection type
     */
    public static final String TYPE = "Network";

    private DataListener dataListener = null;
    private DatagramSocket clientSocket;
    private InetAddress ipAddress;
    private int port;
    private DatagramSocket serverSocket;

    public UdpConnection(String address, int port) throws Exception {
        ipAddress = InetAddress.getByName(address);
        this.port = port;
    }

    @Override
    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }


    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public int write(byte[] buffer) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, ipAddress, port);
        clientSocket.send(sendPacket);
        System.out.println("sent "+sendPacket.getLength());
        return sendPacket.getLength();
    }

    @Override
    public void open() throws IOException {
        clientSocket = new DatagramSocket();
        //serverSocket = new DatagramSocket(9930);
        final Thread t=new Thread(this);
        t.start();
    }

    @Override
    public void close() throws IOException {
        clientSocket.close();
    }

    @Override
    public boolean isOpen() {
        return !clientSocket.isClosed();
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return write(src.array());
    }

    @Override
    public void run() {
        final ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("Starting");
        while (true) {
            try {
                buffer.clear();
                DatagramPacket receivePacket = new DatagramPacket(buffer.array(), buffer.array().length);
                clientSocket.receive(receivePacket);
                buffer.position(receivePacket.getLength());
                buffer.flip();
                dataListener.dataReceived(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
