package controllers;

import game2048.Game2048;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import neuronet.Neuronet;

import javax.swing.*;
import java.io.IOException;

public class Controller implements  ControlledScreen {


    ScreensController myController;
    @FXML
    public TextField tickTimeInputField;

    @FXML
    public TextField hiddenNodesCount;

    @FXML
    public TextField popSizeField;

    @FXML
    private Button goButt;
    private Canvas canvas;

    @FXML
    void onClickGoButt(ActionEvent event) throws IOException {

       // myController.setScreen(Main.neuroNetScreenID);
        myController.unloadScreen(Main.mainScreenID);


        Neuronet net = new Neuronet(Integer.parseInt(hiddenNodesCount.getText()),
                Integer.parseInt(popSizeField.getText()),
                Float.parseFloat(tickTimeInputField.getText()),
                myController);

        //Start NeuroNet
        net.runSimulation(prepareGame());
    }

    private Game2048 prepareGame() {
        Game2048 game2048 = new Game2048();

        JFrame game = new JFrame();
        game.setTitle("2048 Game");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(340, 400);
        game.setResizable(false);

        game.add(game2048);
        game.setLocationRelativeTo(null);
        game.setVisible(true);


        return game2048;
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
