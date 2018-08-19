package it.oop.polito.ftgraph;

import javafx.beans.binding.Bindings;
import javafx.geometry.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CartesianPlot {
	
	private Axes axes;
	
	public CartesianPlot() {
		
		axes = new Axes(
                400, 300,
                -8, 8, 1,
                -6, 6, 1
        );
	}
	
	public Axes getAxes() {
		return this.axes;
	}
	
    public Plot plot(String functionString) throws Exception {
        
        Expression exp = new ExpressionBuilder(functionString).variables("x","y").build();
        
        Plot plot = new Plot(
                exp,
                -8, 8, 0.1,
                axes
        );

        return plot;
    }

    class Axes extends Pane {
        private NumberAxis xAxis;
        private NumberAxis yAxis;

        public Axes(
                int width, int height,
                double xLow, double xHi, double xTickUnit,
                double yLow, double yHi, double yTickUnit
        ) {
            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(width, height);
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            xAxis = new NumberAxis(xLow, xHi, xTickUnit);
            xAxis.setSide(Side.BOTTOM);
            xAxis.setMinorTickVisible(false);
            xAxis.setPrefWidth(width);
            xAxis.setLayoutY(height / 2);

            yAxis = new NumberAxis(yLow, yHi, yTickUnit);
            yAxis.setSide(Side.LEFT);
            yAxis.setMinorTickVisible(false);
            yAxis.setPrefHeight(height);
            yAxis.layoutXProperty().bind(
                Bindings.subtract(
                    (width / 2) + 1,
                    yAxis.widthProperty()
                )
            );

            getChildren().setAll(xAxis, yAxis);
        }

        public NumberAxis getXAxis() {
            return xAxis;
        }

        public NumberAxis getYAxis() {
            return yAxis;
        }
        
    }

    class Plot extends Pane {
        public Plot(
                Expression f,
                double xMin, double xMax, double xInc,
                Axes axes
        ) {
            Path path = new Path();
            path.setStroke(Color.RED);
            path.setStrokeWidth(2);

            path.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
            );

            double x = xMin;
            double y = f.setVariable("x", x).evaluate();

            path.getElements().add(
                    new MoveTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += xInc;
            while (x < xMax) {
                y = f.setVariable("x", x).evaluate();

                path.getElements().add(
                        new LineTo(
                                mapX(x, axes), mapY(y, axes)
                        )
                );

                x += xInc;
            }

            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            getChildren().setAll(axes, path);
        }

        private double mapX(double x, Axes axes) {
            double tx = axes.getPrefWidth() / 2;
            double sx = axes.getPrefWidth() / 
               (axes.getXAxis().getUpperBound() - 
                axes.getXAxis().getLowerBound());

            return x * sx + tx;
        }

        private double mapY(double y, Axes axes) {
            double ty = axes.getPrefHeight() / 2;
            double sy = axes.getPrefHeight() / 
                (axes.getYAxis().getUpperBound() - 
                 axes.getYAxis().getLowerBound());

            return -y * sy + ty;
        }
    }
}
