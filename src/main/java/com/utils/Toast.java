package com.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast {

    private static Stage stage;
    private static Stage toastStage;
    private static int toastDelay;
    private static int fadeInDelay;
    private static int fadeOutDelay;



    public Toast(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Microsoft YaHei", 25));
        text.setFill(Color.WHITE);

        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.75); -fx-padding: 25px;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.setAlwaysOnTop(true);
        toastStage.show();
        Toast.stage = toastStage;
        Toast.toastDelay = toastDelay;
        Toast.fadeInDelay = fadeInDelay;
        Toast.fadeOutDelay = fadeOutDelay;
    }

    public void closeStage(){
        toastStage.hide();
    }

    public void doSomething(NodeCallBack nodeCallBack){
        nodeCallBack.callBackFunction();
        callBack();
    }

    private void callBack(){
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) -> new Thread(() -> {
            try {
                Thread.sleep(toastDelay);
            } catch (InterruptedException ignore) {
                // TODO Auto-generated catch block

            }
            Timeline fadeOutTimeline = new Timeline();
            KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
            fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
            fadeOutTimeline.setOnFinished((aeb) -> stage.close());
            fadeOutTimeline.play();
        }).start());
        fadeInTimeline.play();
    }

}
