package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    public Spinner<Integer> spNumberOfAttractors;

    @FXML
    public Slider slMovePercentage;
    
    @FXML
    public Label lbMovePercentage;

    @FXML
    private Button btSingleIteration;

    @FXML
    private Button btStart;

    @FXML
    public Label lbIterations;

    @FXML
    public Slider slIterationSpeed;

    @FXML
    private Button btResetCanvas;

    @FXML
    private Button btSpreadPoints;

    @FXML
    public Spinner<Integer> spDrawSize;

    @FXML
    public ColorPicker cpDrawColor;

    @FXML
    public CheckBox cbHighlightTarget;

    @FXML
    public Canvas canvas;

    @FXML
    void btActionResetCanvas(ActionEvent event) {
    	Main.resetCanvas();
    }

    @FXML
    void btActionSingleIteration(ActionEvent event) {
    	Main.iteration(slMovePercentage.getValue());
    }

    @FXML
    void btActionSpreadPoints(ActionEvent event) {

    }

    @FXML
    void btActionStart(ActionEvent event) {
    	Main.flipIterationRunning();
    	
    	if(Main.isIterationRunning()){
    		btStart.setText("Stop");
    		btSingleIteration.setDisable(true);
    	}else{
    		btStart.setText("Start");
    		btSingleIteration.setDisable(false);
    	}
    	
    }

    @FXML
    void canvasClick(MouseEvent event) {
    	Main.handlePointClick(event.getX(), event.getY());
    }

    @FXML
    void canvasDrag(MouseEvent event) {
    	Main.handlePointClick(event.getX(), event.getY());
    }

    @FXML
    void canvasPressed(MouseEvent event) {
    	Main.handlePointClick(event.getX(), event.getY());
    }

}
