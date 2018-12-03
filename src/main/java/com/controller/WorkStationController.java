package com.controller;

import com.utils.FileLockManager;
import com.utils.LogUtil;
import com.utils.Toast;
import com.utils.screenUtil.ImageTool;
import com.utils.screenUtil.ScreenCapture;
import com.utils.socketUtil.CallSocketSender;
import com.utils.socketUtil.JsonUtil;
import com.utils.socketUtil.ImageSocketSender;
import com.utils.socketUtil.SocketReceiver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2018年11月01日12:31:23
 * 修正定时请求数据 15s一次 53-60 147-194
 * 2018年11月01日18:51:18
 * 修正提示框不消失的问题
 * 2018年11月15日13:50:13
 * 修正记录日志报错详情
 * 修正接收socket utf-8 改成gbk
 * 退出关闭 socketReceiver,socket发送超时时间3秒
 *
 */
public class WorkStationController {

    public static Stage stage;
    @FXML
    private Button nextCustomerButton; // 下一位
    @FXML
    private Button reCallButton;// 重呼
    @FXML
    private Button screenCaptureButton;//截图
    @FXML
    private Button sendButton;//socket 发送
    @FXML
    private Label queuePosLabel;
    @FXML
    private Label remainingCustomerNumLabel;
    private SocketReceiver socketReceiver = null;
    private Toast toast;

    @FXML
    private void initialize() {
        bindEvent();
        queuePosLabel.setText("");
        remainingCustomerNumLabel.setText("");
        // 监听8888端口返回信息;
        socketReceiver = SocketReceiver.getSocketReceiver();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showData();
            }
        };
        java.util.Timer timer = new Timer();
        timer.schedule(task, 0, 15000);
    }

    private void bindEvent() {
        //截图
        screenCaptureButton.setOnMouseClicked(event -> Platform.runLater(() -> {
            Platform.runLater(() -> stage.setIconified(true));
            SwingUtilities.invokeLater(() -> {
                ScreenCapture capture = new ScreenCapture();
                capture.captureRectangle();
                Platform.runLater(()-> stage.setIconified(false));
                if (capture.getPickedImage() != null) {
                    try {
                        ImageTool.saveAsPNG(capture.getPickedImage(), new File("drawable.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }));

        // 发送截图
        sendButton.setOnMouseClicked(event -> {
            ImageSocketSender sendSocket = ImageSocketSender.getSocketSender();
            sendSocket.sendImage("drawable.png");
        });

        reCallButton.setOnMouseClicked(event -> {
            if (!queuePosLabel.getText().equals("")) {
                CallSocketSender callSocketSender;
                try {
                    callSocketSender = CallSocketSender.getSocketSender();
                } catch (IOException e) {
                    exceptionEvent(e,"socket 连接异常（重新呼叫）。"+e.getMessage());
                    return;
                }
                String nextString;
                try {
                    nextString = callSocketSender.sendJson(JsonUtil.parseString("recalled", queuePosLabel.getText(), ""));
                    System.out.println("重新呼叫："+nextString);
                } catch (IOException e) {
                    exceptionEvent(e,"socket 发送重新呼叫的数据异常。");
                    return;
                }
                if (!nextString.equals("")){
                    JsonUtil.analysisJson(nextString);
                    Platform.runLater(()->{
                        if (JsonUtil.getQueuePos().get()!=null&&JsonUtil.getQueuePos().get().length()>0){
                            queuePosLabel.setText(JsonUtil.getQueuePos().get());
                        }
                        if (JsonUtil.getRemainingCustomerNum().get()!=null && JsonUtil.getRemainingCustomerNum().get().length()>0){
                            remainingCustomerNumLabel.textProperty().bind(JsonUtil.getRemainingCustomerNum());
                            remainingCustomerNumLabel.setText(JsonUtil.getRemainingCustomerNum().get());
                        }
                    });

                }
            } else {
                String message = "当前无呼叫病人。";
                showToast(message);
            }
        });

        nextCustomerButton.setOnMouseClicked(event -> {
            CallSocketSender callSocketSender;
            try {
                callSocketSender = CallSocketSender.getSocketSender();
            } catch (IOException e) {
                exceptionEvent(e,"socket 连接异常（下一位呼叫）。"+e.getMessage());
                return;
            }
            String nextString;
            try {
                nextString = callSocketSender.sendJson(JsonUtil.parseString("next", "", ""));
                System.out.println("下一位呼叫："+nextString);
            } catch (IOException e) {
                exceptionEvent(e,"socket 发送下一位的数据异常。");
                return;
            }
            JsonUtil.analysisJson(nextString);
            Platform.runLater(()->{
                queuePosLabel.setText(JsonUtil.getQueuePos().get());
//                    remainingCustomerNumLabel.textProperty().bind(JsonUtil.getRemainingCustomerNum());
                remainingCustomerNumLabel.setText(JsonUtil.getRemainingCustomerNum().get());
            });
        });

        stage.setOnCloseRequest(event -> {
            FileLockManager fileLockManager = new FileLockManager("lock.lock");
            try {
                fileLockManager.unLock();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("注意");
            alert.setContentText("此操作会清空当前排队号，是否确认退出？");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                CallSocketSender callSocketSender;
                try {
                    callSocketSender = CallSocketSender.getSocketSender();
                } catch (IOException e) {
                    exceptionEvent(e,"清空数据时，socket 连接异常。");
                    event.consume();

                    return;
                }
                try {
                    callSocketSender.sendJson(JsonUtil.parseString("finish","",""));
                    System.exit(0);
                } catch (IOException e) {
                    exceptionEvent(e,"socket 发送清除数据的数据异常。");
                    event.consume();
                }

            } else {
                event.consume();
            }
        });
    }

    private void exceptionEvent(Exception e,String message){
        LogUtil.markLog(2, message);
        Platform.runLater(()-> showToast(e.getMessage()));
    }

    private void showData(){
        CallSocketSender callSocketSender;
        try {
            callSocketSender = CallSocketSender.getSocketSender();
        } catch (IOException e) {
            LogUtil.markLog(2,"后台socket取数据异常。"+e.getMessage());
            return;
        }
        String nextString;
        try {
            nextString = callSocketSender.sendJson(JsonUtil.parseString("find","",""));
        } catch (IOException e) {
            LogUtil.markLog(2,"后台socket取数据异常。"+e.getMessage());
            return;
        }
        if (nextString!=null) {
            if (!nextString.equals("")){
                JsonUtil.analysisJson(nextString);
                Platform.runLater(()-> remainingCustomerNumLabel.setText(JsonUtil.getRemainingCustomerNum().get()));
            }
        }

    }

    private void showToast(String msg) {
        if (toast != null) {
            toast.closeStage();
            toast = new Toast(new Stage(), msg, 2500, 500, 500);
            toast.doSomething(() -> LogUtil.markLog(2, msg));
        } else {
            toast = new Toast(new Stage(), msg, 2500, 500, 500);
            toast.doSomething(() -> LogUtil.markLog(2, msg));
        }
    }

}
