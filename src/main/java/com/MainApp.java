package com;

import com.controller.WorkStationController;
import com.utils.FileLockManager;
import com.utils.LogUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public void start(Stage primaryStage) {
        WorkStationController.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/fxml/WorkStationBasePane.fxml"));
        try {
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileLockManager fileLockManager = new FileLockManager("lock.lock");
        try {
            boolean isLock = fileLockManager.Lock();
            if (isLock){
                launch();
            } else {
                LogUtil.markLog(2,"程序不允许多开");
                System.exit(0);
            }
        } catch (IOException ignore) {
        }
    }

}
