package com.node;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageAlert {

	public MessageAlert() {

	}

	public static void showAlert(Integer type, String context) {
		Stage window = new Stage();
		window.setTitle("注意");
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(400);
		window.setMinHeight(150);
		Button button = new Button("确认");
		button.setPrefWidth(100);
		button.setPrefHeight(60);
		button.setStyle("-fx-font-size: 24px;");
		button.setOnAction(e -> window.close());
		Label label = new Label(context);
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label , button);
		layout.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-size: 24px;");
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.setAlwaysOnTop(true);
		//使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}
}
