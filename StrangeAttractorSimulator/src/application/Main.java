package application;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;


public class Main extends Application {
	public static List<Point> attractorPoints = new ArrayList<>();
	public static List<Point> calculatedPoints = new ArrayList<>();
	public static Point targetPoint = new Point(400, 400);
	
	public static Random rand = new Random();
	
	private static boolean iterationRunning = false;
	public static void flipIterationRunning(){
		iterationRunning = !iterationRunning;
	}
	public static boolean isIterationRunning(){
		return iterationRunning;
	}
	
	public static void resetCanvas(){
		calculatedPoints.clear();
	}
	
	public static void handlePointClick(double x, double y){
		double smallestDistance = Double.POSITIVE_INFINITY;
		Point closestPoint = null;
		List<Point> handledPoints = new ArrayList<>(attractorPoints);
		handledPoints.add(targetPoint);
		
		for(Point p : handledPoints){
			double dist = p.distance(x, y);
			if(dist<smallestDistance){
				smallestDistance = dist;
				closestPoint = p;
			}
		}
		
		closestPoint.set(x, y);
	}
	
	public static void iteration(double movePercentage){
		Point randomAttractor = attractorPoints.get(rand.nextInt(attractorPoints.size()));
		double diffX = randomAttractor.x - targetPoint.x;
		double diffY = randomAttractor.y - targetPoint.y;
		
		double newX = targetPoint.x + diffX * movePercentage;
		double newY = targetPoint.y + diffY * movePercentage;
		

		calculatedPoints.add(new Point(targetPoint.x, targetPoint.y));
		targetPoint.set(newX, newY);
	}
	
	public static void createAttractorPoints(int num){
		attractorPoints.clear();
		for(int i = 0; i<num; i++){
			double x = Math.sin((double)i/num * Math.PI * 2 + Math.PI) * 350 + 400;
			double y = Math.cos((double)i/num * Math.PI * 2 + Math.PI) * 350 + 400;
			attractorPoints.add(new Point(x, y));
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			createAttractorPoints(3);
			
			System.out.println("Backfisch");
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Layout.fxml"));
			Parent root = fxmlLoader.load();
			Controller controller = fxmlLoader.getController();
			Canvas c = controller.canvas;
			ColorPicker cpDrawColor = controller.cpDrawColor;
			cpDrawColor.setValue(Color.RED);
			GraphicsContext gc = c.getGraphicsContext2D();
			
			controller.spNumberOfAttractors.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 3));
			controller.spNumberOfAttractors.valueProperty().addListener(new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable,
						Number oldValue, Number newValue) {
					createAttractorPoints(newValue.intValue());
				}
				
			});
			
			controller.spDrawSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 3));
			
			controller.slMovePercentage.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable,
						Number oldValue, Number newValue) {
					controller.lbMovePercentage.setText("" + Math.round(newValue.doubleValue() * 100.) / 100.);
				}
			});
			
			new AnimationTimer() {
				
				private double amountOfIterationsLeftFromPreviousFrames = 0;
				
				@Override
				public void handle(long now) {
					gc.setFill(Color.WHITE);
					gc.fillRect(0, 0, c.getWidth(), c.getHeight());
					gc.setFill(cpDrawColor.getValue());
					for(Point p : calculatedPoints){
						p.draw(gc, controller.spDrawSize.getValue());
					}
					gc.setFill(Color.BLACK);
					for(Point p : attractorPoints){
						p.draw(gc, 10);
					}
					if(controller.cbHighlightTarget.isSelected()){
						gc.setFill(Color.GREEN);
						targetPoint.draw(gc, 10);
						gc.fillText("Target Point", targetPoint.x + 10, targetPoint.y + 5);
					}
					
					if(iterationRunning){
						double val = controller.slIterationSpeed.valueProperty().get();
						double amountOfIterations = Math.pow(val, 3) * 1000 + amountOfIterationsLeftFromPreviousFrames;
						for(int i = 0; i<amountOfIterations && amountOfIterations > 1; i++){
							iteration(controller.slMovePercentage.getValue());
						}
						
						amountOfIterationsLeftFromPreviousFrames = amountOfIterations - (int)amountOfIterations;
					}
					
					controller.lbIterations.setText(""+calculatedPoints.size());
						
				}
				
			}.start();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Strange Attractor Simulator - v 0.0.1 - (c) Brotcrunsher");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
