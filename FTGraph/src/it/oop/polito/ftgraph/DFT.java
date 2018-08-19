package it.oop.polito.ftgraph;
import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class DFT {
	
	private Axes axes;
	
	public DFT() {
		
		this.axes = new Axes(
                400, 300,
                -8, 8, 1,
                -40, 40, 1
        );
	}
	
	public DFTPlot plot(String functionString) {
		
		Expression exp = new ExpressionBuilder(functionString).variables("x","y").build();
		DFTPlot plot = new DFTPlot(exp, -8, 8, 0.1, axes);
		return plot;
	}
	
	class DFTPlot extends Pane {
        public DFTPlot(
                Expression f,
                double xMin, double xMax, double xInc,
                Axes axes
        ) {
            Path realPath = new Path();
            Path imgPath = new Path();
         
            realPath.setStroke(Color.RED);
            realPath.setStrokeWidth(2);
            imgPath.setStroke(Color.BLUE);
            imgPath.setStrokeWidth(2);

            realPath.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
             
            );
            
            imgPath.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
             
            );

            
            int n = (int)((xMax - xMin) / xInc);
            double[] yArray = new double[n];
            double[] realArray = new double[n];
            double[] imgArray = new double[n];
            
            for (int i = 0; i < n; i++) {
            	try {
            		yArray[i] = f.setVariable("x", xMin + i * xInc).evaluate();
            	} catch (ArithmeticException ex0) {
            		yArray[i] = 0;
            	}
            	realArray[i] = 0;
            	imgArray[i] = 0;
            }

            
            for (int i = 0; i < n; i++) {
    			double real = 0;
    			double img = 0;
    			for (int t = 0; t < n; t++) {
    				double angle = 2 * Math.PI * t * i / n;
    				try {
    					real +=  yArray[t] * Math.cos(angle);
    				} catch (ArithmeticException ex) {
    					real = 0;
    				}
    				try {
    					img += -1 * yArray[t] * Math.sin(angle);
    				} catch (ArithmeticException ex) {
    					img = 0;
    				}
    			}
    			realArray[i] = real;
    			imgArray[i] = img;
    			//System.out.println(real + " + i " + img);
            }

            realPath.getElements().add(
                    new MoveTo(
                            mapX(xMin, axes), mapY(realArray[0], axes)
                    )
            );
            imgPath.getElements().add(
                    new MoveTo(
                            mapX(xMin, axes), mapY(imgArray[0], axes)
                    )
            );
            
            double x = xMin;

            for (int i = 0; i < n; i++) {
            	realPath.getElements().add(new LineTo(mapX(x, axes), mapY(realArray[i], axes)));
            	imgPath.getElements().add(new LineTo(mapX(x, axes), mapY(imgArray[i], axes)));
            	x += xInc;
            }
            

            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            getChildren().setAll(axes, realPath, imgPath);
            
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
}
