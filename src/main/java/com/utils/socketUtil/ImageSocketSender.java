package com.utils.socketUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.utils.IniReadUtil;
import com.utils.LogUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Socket连接
 */
public class ImageSocketSender {

    private static Socket socket;
    private static BufferedWriter outputStream;

    private ImageSocketSender() {
        Map<String, String> map = IniReadUtil.readIniGetImageSocketConfig();
        String portStr = map.get("port");
        String host = map.get("ip");
        Integer port = Integer.parseInt(portStr);
        // 初始化
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        try {
            socket.connect(socketAddress, 3000);
            socket.setSoTimeout(3000);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.markLog(2,"socket 连接错误，检查配置文件。");
        }
    }

    public void sendImage(String fileName) {
        String imageInfo = Base64Util.ImageToBase64ByLocal(fileName);
        Map<String,String> jsonMap = new HashMap<>();
        jsonMap.put("image",imageInfo);
        String jsonString = JSONObject.toJSONString(jsonMap,SerializerFeature.WriteMapNullValue);
        try {
            outputStream.write(jsonString);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.markLog(2,"socket 发送数据异常。");
        }
    }


    public static ImageSocketSender getSocketSender() {
        ImageSocketSender socketSender = new ImageSocketSender();
        try {
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.markLog(2,"socket 获取流异常。");
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return socketSender;
    }


}