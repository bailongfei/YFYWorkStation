package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class  HintWindowController {

    private Stage window;
    private boolean closeFlag = false;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    private void show(){
        window = new Stage();
        window.setTitle("注意");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/hintWindow.fxml"));
        AnchorPane layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.setAlwaysOnTop(true);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        show();
        bindEvent();
    }

    private void bindEvent(){
        yesButton.setOnMouseClicked(event -> closeFlag = true);

        noButton.setOnMouseClicked(event -> closeFlag = false);

        window.setOnCloseRequest(event -> closeFlag = false);
    }

    boolean isCloseFlag() {
        return closeFlag;
    }

    Stage getWindow() {
        return window;
    }
}
