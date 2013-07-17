package com.aico.remote;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * User: vk
 * Date: 7/1/13
 * Time: 12:44 AM
 */
public interface Connection {
    void setDataListener(DataListener dataListener);

    String getType();

    int write(byte[] buffer) throws IOException;

    void open() throws IOException;

    void close() throws IOException;

    boolean isOpen();

    int write(ByteBuffer src) throws IOException;
}
