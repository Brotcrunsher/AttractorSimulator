package application;

import javafx.scene.canvas.GraphicsContext;

public class Point {
	public double x;
	public double y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double distance(double otherX, double otherY){
		double diffX = otherX - this.x;
		double diffY = otherY - this.y;
		return Math.sqrt(diffX*diffX + diffY*diffY);
	}
	
	public void draw(GraphicsContext gc, double size){
		gc.fillOval(x-size/2, y-size/2, size, size);
	}
}
