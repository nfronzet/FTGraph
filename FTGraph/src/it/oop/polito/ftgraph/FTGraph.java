package it.oop.polito.ftgraph;

import it.oop.polito.ftgraph.CartesianPlot.Plot;
import it.oop.polito.ftgraph.DFT.DFTPlot;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FTGraph extends Application {

	private Plot plotWindow;
	private DFTPlot FTPlotWindow;
	
	public static void main(String[] args) {
		Application.launch();
	}

	@Override
	public void start(Stage window) throws Exception {
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10,10,10,10));
		layout.setVgap(8);
		layout.setHgap(15);
		
		Label fLabel = new Label("Function graph");
		Label tLabel = new Label("Fourier transform graph");
		GridPane.setConstraints(fLabel, 0, 0);
		GridPane.setConstraints(tLabel, 1, 0);
		
		CartesianPlot cartPlot = new CartesianPlot();
		plotWindow = cartPlot.plot("0");
		GridPane.setConstraints(plotWindow, 0, 1);
		DFT FTPlot = new DFT();
		FTPlotWindow = FTPlot.plot("0");
		GridPane.setConstraints(FTPlotWindow, 1, 1);
		
		TextField functionField = new TextField();
		GridPane.setConstraints(functionField, 0, 2);
		
		Button plotButton = new Button("Plot function and transform");
		GridPane.setConstraints(plotButton, 0, 3);
		plotButton.setOnAction(e -> {
			String functionString = functionField.getText();
			try {
				layout.getChildren().remove(plotWindow);
				plotWindow = cartPlot.plot(functionString);
				GridPane.setConstraints(plotWindow, 0, 1);
				layout.getChildren().add(plotWindow);
				
				layout.getChildren().remove(FTPlotWindow);
				FTPlotWindow = FTPlot.plot(functionString);
				GridPane.setConstraints(FTPlotWindow, 1, 1);
				layout.getChildren().add(FTPlotWindow);
			} catch (Exception ex) {
				AlertBox.display("Plot error", "Error detected with error code: " + ex.toString());
			}
		});
		
		Button clearButton = new Button("Clear");
		GridPane.setConstraints(clearButton, 0, 3);
		GridPane.setHalignment(clearButton, HPos.CENTER);
		clearButton.setOnAction(e -> {
			try {
				layout.getChildren().remove(plotWindow);
				plotWindow = cartPlot.plot("0");
				GridPane.setConstraints(plotWindow, 0, 1);
				layout.getChildren().add(plotWindow);
				
				layout.getChildren().remove(FTPlotWindow);
				FTPlotWindow = FTPlot.plot("0");
				GridPane.setConstraints(FTPlotWindow, 1, 1);
				layout.getChildren().add(FTPlotWindow);
			} catch (Exception ex) {
				AlertBox.display("Clear error", "Error detected with error code: " + ex.toString());
			}
		});
		
		
		layout.getChildren().addAll(fLabel, tLabel, plotWindow, FTPlotWindow, functionField, plotButton, clearButton);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.setTitle("FTGraph");
		window.show();
	}
	
}
