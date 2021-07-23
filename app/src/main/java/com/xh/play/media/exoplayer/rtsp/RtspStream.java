package com.xh.play.media.exoplayer.rtsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RtspStream {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public RtspStream(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public int read(byte[] buff, int off, int len) throws IOException {
        return inputStream.read(buff, off, len);
    }

    public void writ(byte[] buff, int off, int len) throws IOException {
        outputStream.write(buff, off, len);
    }

    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
