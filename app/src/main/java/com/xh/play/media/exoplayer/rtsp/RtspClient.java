package com.xh.play.media.exoplayer.rtsp;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RtspClient {
    private static final String VERSION = " RTSP/1.0\r\n";
    private static final String RTSP_OK = "RTSP/1.0 200 OK";
    private static final String CSEQ = "Cseq: ";
    private static final String ERROR_CODE = "code:%s";
    private BufferedReader mBufferreader;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private Socket mSocket;
    private static SDPInfo sdpInfo;
    private static boolean Describeflag = false; //used to get SDP info
    private String mSession;
    private String UserAgent;
    private boolean isTCPtranslate = true;
    private String address;
    private String host;
    private RtspStream rtspStream;
    private String authorPassword;

    public RtspClient(String host, int port, String url) throws IOException {
        address = url;
        this.host = host;
        mSocket = new Socket(host, port);
        mInputStream = mSocket.getInputStream();
        mBufferreader = new BufferedReader(new InputStreamReader(mInputStream));
        mOutputStream = mSocket.getOutputStream();
        sdpInfo = new SDPInfo();
        doOption();
        doDescribe();
        doSetup();
        doPlay();
//        connect();

    }

    public void setAuthorPassword(String author, String pass) {
        if(author!=null&&!author.isEmpty()&&pass!=null&&!pass.isEmpty()){
            authorPassword= Base64.encodeToString((author+":"+authorPassword).getBytes(), Base64.DEFAULT);
        }
    }

    public void doOption() throws IOException {
        System.err.println("doOption");
        StringBuilder sb = new StringBuilder();
        sb.append("OPTIONS ");
        sb.append(this.address.substring(0, address.lastIndexOf("/")));
        sb.append(VERSION);
        send(heard(sb, 1));
        Response mResponse = Response.parseResponse(mBufferreader);
        if (mResponse.state != 200)
            throw new IOException(String.format(ERROR_CODE, mResponse.state));
    }

    private void doDescribe() throws IOException {
        System.err.println("doDescribe");
        StringBuilder sb = new StringBuilder();
        sb.append("DESCRIBE ");
        sb.append(this.address);
        sb.append(VERSION);
        Describeflag = true;
        send(heard(sb, 2));
        Response mResponse = Response.parseResponse(mBufferreader);
        if (mResponse.state != 200)
            throw new IOException(String.format(ERROR_CODE, mResponse.state));
    }

    private void doSetup() throws IOException {
        System.err.println("doSetup");
        StringBuilder sb = new StringBuilder();
        sb.append("SETUP ");
        sb.append(this.address);
        sb.append("/");
        sb.append(sdpInfo.videoTrack);
        sb.append(VERSION);
        if (isTCPtranslate)
            sb.append("Transport: RTP/AVP/TCP;unicast;client_port=55640-55641\r\n");
        else
            sb.append("Transport: RTP/AVP/UDP;unicast;client_port=55640-55641\r\n");
        send(heard(sb, 3));
        Response mResponse = Response.parseResponse(mBufferreader);
        if (mResponse.state == 200) {
            Matcher matcher = Response.regexSessionWithTimeout.matcher(
                    mResponse.headers.
                            get("session")
            );
            if (matcher.find()) mSession = matcher.group(1);
            else mSession = mResponse.headers.get("session");
            System.out.println("the session is " + mSession);
            if (isTCPtranslate) {
                matcher = Response.regexTCPTransport.matcher(mResponse.headers.get("transport"));
                if (matcher.find()) {
                    System.out.println("The client port is:" + matcher.group(1) + " ,the server prot is:" + (isTCPtranslate ? "null" : matcher.group(2)) + "...");
                    rtspStream = new RtspStream(new Socket(host, Integer.parseInt(matcher.group(1))));
//                    mParams.rtpPort = Integer.parseInt(matcher.group(1));
//                    if (!isTCPtranslate) mParams.serverPort = Integer.parseInt(matcher.group(2));
//
//                    //prepare for the video decoder
//                    mH264Stream = new H264Stream(sdpInfo);
//                    mH264Stream.setSurfaceView(mSurfaceView);
//
//                    if (isTCPtranslate)
//                        mRtpSocket = new RtpSocket(isTCPtranslate, mParams.rtpPort, mParams.host, -1, TRACK_VIDEO);
//                    else
//                        mRtpSocket = new RtpSocket(isTCPtranslate, mParams.rtpPort, mParams.host, mParams.serverPort, TRACK_VIDEO);
//                    mRtpSocket.startRtpSocket();
//                    mRtpSocket.setStream(mH264Stream);
                } else
                    rtspStream = new RtspStream(mSocket);
            }
        } else {
            throw new IOException(String.format(ERROR_CODE, mResponse.state));
        }


    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (rtspStream != null)
            return rtspStream.read(buffer, offset, readLength);
        return mInputStream.read(buffer, offset, readLength);
    }

    private void doPlay() throws IOException {
        System.err.println("doPlay");
        StringBuilder sb = new StringBuilder();
        sb.append("PLAY ");
        sb.append(this.address);
        sb.append(VERSION);
        sb.append("Range: npt=0.000-\r\n");
        send(heard(sb, 4));
        Response.parseResponse(mBufferreader);

    }

    private void doPause() throws IOException {
        System.err.println("doPause");
        StringBuilder sb = new StringBuilder("PAUSE ").append(address).append(VERSION);
        send(heard(sb, 5));
        Response.parseResponse(mBufferreader);
    }

    public void doRecord() throws IOException {
        System.err.println("doRecord");
        StringBuilder sb = new StringBuilder("RECORD ").append(address).append(VERSION);
        send(heard(sb, 6));
        Response.parseResponse(mBufferreader);
    }

    private void doTeardown() throws IOException {
        System.err.println("doTeardown");
        StringBuilder sb = new StringBuilder("TEARDOWN ");
        sb.append(address).append("/").append(sdpInfo.videoTrack).append(VERSION);
        send(heard(sb, 7));
    }

    private void doGetParameter() throws IOException {
        System.err.println("doGetParameter");
        StringBuilder sb = new StringBuilder("GET_PARAMETER ");
        sb.append(address).append("/").append(sdpInfo.videoTrack).append(VERSION);
        send(heard(sb, 8));
    }

    private String heard(StringBuilder sb, int cseq) {
        sb.append(CSEQ).append(cseq).append("\r\n");
        if(authorPassword!=null&&!authorPassword.isEmpty())
            sb.append("Authorization: Basic ").append(authorPassword).append("\r\n");
        if (UserAgent != null && !UserAgent.isEmpty())
            sb.append("UserAgent: ").append(UserAgent).append("\r\n");
        if (mSession != null && !mSession.isEmpty())
            sb.append("Session: ").append(mSession).append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }


    private void send(String msg) throws IOException {
        System.out.println(msg);
        send(msg.getBytes("UTF-8"));
    }

    private void send(byte[] buff) throws IOException {
        mOutputStream.write(buff);
    }

    public void close() {
        System.out.println("关闭连接");
        rtspStream.close();
        try {
            mBufferreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class SDPInfo {
        public boolean audioTrackFlag;
        public boolean videoTrackFlag;
        public String videoTrack;
        public String audioTrack;
        public String SPS;
        public String PPS;
        public int packetizationMode;
    }

    static class Response {

        public static final Pattern regexStatus = Pattern.compile("RTSP/\\d.\\d (\\d+) .+", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexHeader = Pattern.compile("(\\S+): (.+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexUDPTransport = Pattern.compile("client_port=(\\d+)-\\d+;server_port=(\\d+)-\\d+", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexTCPTransport = Pattern.compile("client_port=(\\d+)-\\d+;", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSessionWithTimeout = Pattern.compile("(\\S+);timeout=(\\d+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPgetTrack1 = Pattern.compile("trackID=(\\d+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPgetTrack2 = Pattern.compile("control:(\\S+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPmediadescript = Pattern.compile("m=(\\S+) .+", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPpacketizationMode = Pattern.compile("packetization-mode=(\\d);", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPspspps = Pattern.compile("sprop-parameter-sets=(\\S+),(\\S+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPlength = Pattern.compile("Content-length: (\\d+)", Pattern.CASE_INSENSITIVE);
        public static final Pattern regexSDPstartFlag = Pattern.compile("v=(\\d)", Pattern.CASE_INSENSITIVE);

        public int state;
        public static HashMap<String, String> headers = new HashMap<>();

        public static Response parseResponse(BufferedReader input) throws IOException {
            Response response = new Response();
            String line;
            Matcher matcher;
            int sdpContentLength = 0;
            if ((line = input.readLine()) == null) throw new IOException("Connection lost");
            matcher = regexStatus.matcher(line);
            if (matcher.find())
                response.state = Integer.parseInt(matcher.group(1));
            else
                while ((line = input.readLine()) != null) {
                    matcher = regexStatus.matcher(line);
                    if (matcher.find()) {
                        response.state = Integer.parseInt(matcher.group(1));
                        break;
                    }
                }
            System.out.println("The response state is: " + response.state);

            int foundMediaType = 0;
            int sdpHaveReadLength = 0;
            boolean sdpStartFlag = false;

            while ((line = input.readLine()) != null) {
                if (line.length() > 3 || Describeflag) {
                    System.out.println("The line is: " + line + "...");
                    matcher = regexHeader.matcher(line);
                    if (matcher.find())
                        headers.put(matcher.group(1).toLowerCase(Locale.US), matcher.group(2)); //$ to $

                    matcher = regexSDPlength.matcher(line);
                    if (matcher.find()) {
                        sdpContentLength = Integer.parseInt(matcher.group(1));
                        sdpHaveReadLength = 0;
                    }
                    //Here is trying to get the SDP information from the describe response
                    if (Describeflag) {
                        matcher = regexSDPmediadescript.matcher(line);
                        if (matcher.find())
                            if (matcher.group(1).equalsIgnoreCase("audio")) {
                                foundMediaType = 1;
                                sdpInfo.audioTrackFlag = true;
                            } else if (matcher.group(1).equalsIgnoreCase("video")) {
                                foundMediaType = 2;
                                sdpInfo.videoTrackFlag = true;
                            }

                        matcher = regexSDPpacketizationMode.matcher(line);
                        if (matcher.find()) {
                            sdpInfo.packetizationMode = Integer.parseInt(matcher.group(1));
                        }

                        matcher = regexSDPspspps.matcher(line);
                        if (matcher.find()) {
                            sdpInfo.SPS = matcher.group(1);
                            sdpInfo.PPS = matcher.group(2);
                        }

                        matcher = regexSDPgetTrack1.matcher(line);
                        if (matcher.find())
                            if (foundMediaType == 1)
                                sdpInfo.audioTrack = "trackID=" + matcher.group(1);
                            else if (foundMediaType == 2)
                                sdpInfo.videoTrack = "trackID=" + matcher.group(1);


                        matcher = regexSDPgetTrack2.matcher(line);
                        if (matcher.find())
                            if (foundMediaType == 1) sdpInfo.audioTrack = matcher.group(1);
                            else if (foundMediaType == 2) sdpInfo.videoTrack = matcher.group(1);


                        matcher = regexSDPstartFlag.matcher(line);
                        if (matcher.find()) sdpStartFlag = true;
                        if (sdpStartFlag) sdpHaveReadLength += line.getBytes().length + 2;
                        if ((sdpContentLength < sdpHaveReadLength + 2) && (sdpContentLength != 0)) {
                            Describeflag = false;
                            sdpStartFlag = false;
                            System.out.println("The SDP info: "
                                    + (sdpInfo.audioTrackFlag ? "have audio info.. " : "haven't the audio info.. ")
                                    + ";" + (sdpInfo.audioTrackFlag ? (" the audio track is " + sdpInfo.audioTrack) : ""));
                            System.out.println("The SDP info: "
                                    + (sdpInfo.videoTrackFlag ? "have video info.. " : "haven't the vedio info..")
                                    + (sdpInfo.videoTrackFlag ? (" the video track is " + sdpInfo.videoTrack) : "")
                                    + ";" + (sdpInfo.videoTrackFlag ? (" the video SPS is " + sdpInfo.SPS) : "")
                                    + ";" + (sdpInfo.videoTrackFlag ? (" the video PPS is " + sdpInfo.PPS) : "")
                                    + ";" + (sdpInfo.videoTrackFlag ? (" the video packetization mode is " + sdpInfo.packetizationMode) : ""));
                            break;
                        }
                    }
                } else {
                    break;
                }

            }

            if (line == null) throw new IOException("Connection lost");

            return response;
        }
    }
}
