package it.oop.polito.ftgraph;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class AlertBox {
	public static void display(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL); //Blocks interaction with other windows
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label(message);
		Button close = new Button("Close");
		close.setOnAction(e -> window.close());
		
		VBox layout = new VBox(20);
		layout.getChildren().addAll(label,close);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.setResizable(false);
		window.showAndWait(); //Halts processes in other windows, used in conjunction with Modality.APPLICATION_MODAL
	}
}
