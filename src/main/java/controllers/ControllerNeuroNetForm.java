package controllers;

import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Scorpa on 12/22/2015.
 */

public class ControllerNeuroNetForm implements ControlledScreen, Initializable {
    ScreensController myController;

    public GraphicsContext GC;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private Canvas neuroCanvas;

    @FXML
    private Label totalReproductionLabel;

    @FXML
    private Label avgPopFittLabel;

    @FXML
    private Label newCreatureLabel;

    @FXML
    private Label childOfLabel;

    @FXML
    private Label totalMutLabel;

    @FXML
    private Label genNumLabel;

    @FXML
    private Label fitnessLabel;

    @FXML
    private Label totalPopLabel;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    public void updateTotalPopLabel(int data) { totalPopLabel.setText(String.valueOf(data));}
    public void updateTotalReproductionLabel(int data) {
        totalReproductionLabel.setText(String.valueOf(data));
    }

    public void updateAvgPopFittLabel(double data) {
        avgPopFittLabel.setText(String.valueOf(data));
    }

    public void updateNewCreatureLabel(int data) {
        newCreatureLabel.setText(String.valueOf(data));
    }

    public void updateChildOfLabel(String data) {
        childOfLabel.textProperty().set(data);

    }

    public void updateTotalMutLabel(int data) {
        totalMutLabel.setText(String.valueOf(data));
    }

    public void updateGenNumLabel(int data) {
        genNumLabel.setText(String.valueOf(data));
    }

    public void updateFitnessLabel(double data) {
        fitnessLabel.setText(String.valueOf(data));
    }


    public void drawLines(double x1, double y1, double x2, double y2, double width) {
        this.GC.setLineWidth(width);
        this.GC.strokeLine(x1, y1, x2, y2);
    }

    public void drawNodes(Color colour, double xPos, double yPos,double width, double height) {
        GC.setStroke(Color.BLACK);
        GC.setLineWidth(2.0D);
        GC.setFill(colour);
        GC.fillOval(xPos, yPos, width,height);
        GC.strokeOval(xPos, yPos, width,height);

    }

    @PostConstruct
    public void init(){
        totalMutLabel.setText("0");
        genNumLabel.setText("0");
        totalReproductionLabel.setText("0");
        fitnessLabel.setText("0.0");
        avgPopFittLabel.setText("0.0");
        genNumLabel.setText("0");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.GC = neuroCanvas.getGraphicsContext2D();
           }

    public void setStrokeColor(Color colour) {
        this.GC.setStroke(colour);
    }

    public void updateBestScoreLabel(int bestScore) {
        bestScoreLabel.setText(String.valueOf(bestScore));
    }
}
