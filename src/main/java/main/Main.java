package main;

import controllers.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;



public  class Main extends Application {


    public static String mainScreenID = "mainScreen";
    public static String mainScreenFile = "/scenes/MainForm.fxml";
    public static String neuroNetScreenID = "NeuroNetScreen";
    public static String neuroNetScreenFile = "/scenes/NeuroNetForm.fxml";
    @Override
    public void start(Stage primaryStage) throws Exception{


        ScreensController mainController = new ScreensController();
        mainController.loadScreen(mainScreenID,mainScreenFile);
        mainController.loadScreen(Main.neuroNetScreenID, Main.neuroNetScreenFile);

        mainController.setScreen(Main.mainScreenID);

        Group root = new Group();
        root.getChildren().addAll(mainController);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
