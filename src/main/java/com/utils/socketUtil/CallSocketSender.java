package com.utils.socketUtil;

import com.utils.IniReadUtil;
import com.utils.LogUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

public class CallSocketSender {

    private static Socket socket;
    private static BufferedWriter outputStream;

    private CallSocketSender() {
        Map<String, String> map = IniReadUtil.readIniGetCallSocketConfig();
        String portStr = map.get("port");
        String host = map.get("ip");
        System.out.println(host);
        Integer port = Integer.parseInt(portStr);
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        try {
            socket.connect(socketAddress, 3000);
            socket.setSoTimeout(3000);
        } catch (IOException ignored) {
            LogUtil.markLog(2, "socket 连接错误，检查配置文件。");
        }
    }

    public String sendJson(String jsonString) throws IOException {
        outputStream.write(jsonString);
        outputStream.flush();
        socket.shutdownOutput();
        System.out.println(socket.isClosed());
        if (!socket.isClosed()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = br.readLine();
            socket.shutdownInput();
            if (msg != null) {
                return msg;
            }
        }
        return null;
    }

    public static CallSocketSender getSocketSender() throws IOException {
        CallSocketSender socketSender = new CallSocketSender();
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        return socketSender;
    }

}
