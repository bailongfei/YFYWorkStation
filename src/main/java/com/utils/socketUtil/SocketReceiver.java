package com.utils.socketUtil;

import com.utils.IniReadUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

public class SocketReceiver {

    private static SocketReceiver socketReceiver;
    private ServerSocket ss = null;
    private Thread readThread;
    private static boolean flag = true;

    private SocketReceiver() {
        try {
            int port;
            Integer readPort = IniReadUtil.readIniGetListenerPort();
            if (readPort != null) {
                port = readPort;
            } else {
                port = 8888;
            }
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            readThread = new Thread(() -> {
                while (flag) {
                    try {
                        Socket s = ss.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        try {
                            String msg = br.readLine();
                            if (msg != null) {
                                System.out.println(msg);
                                String stringCode = URLDecoder.decode(msg, "gbk");
                                Platform.runLater(() -> showToast(stringCode));
                            }
                            s.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readThread.start();
        });
    }

    public static SocketReceiver getSocketReceiver() {
        if (socketReceiver == null) {
            socketReceiver = new SocketReceiver();
        }
        return socketReceiver;
    }


    private void showToast(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("注意");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
