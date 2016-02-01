package neuronet;

import game2048.Game2048;
import controllers.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Scorpa on 12/18/2015.
 */
public class Neuronet {

    //Constants
    public static final int INPUTS_COUNT = 16;
    public static final int OUTPUTS_COUNT = 4;
    private static final double SCORE_WEIGHT = 0.4;
    private static final double MAX_TILE_WEIGHT = 0.6;
    public static int HIDDEN_COUNT;
    public static int POP_SIZE;
    public static float TICK_RATE;
    public static String action = null;
    private final ScreensController screenChangerController;
    private final String currentScreen = Main.neuroNetScreenID;
    private Scene scene;


    //Bunch of variables for neuroNet statistics
    private int reproductionCount = 0;
    private int generationCount = 0;
    private int currentCreature = 0;
    private int parent1 = 0;
    private int parent2 = 0;
    private int mutations = 0;
    private double averageFitness = 0.0D;
    private Game2048 game2048;
    private int bestScore = 0;


    private ArrayList<Double> fitLevels = new ArrayList();
    private ArrayList<Double> averageFitLevels = new ArrayList();
    private Creature[] creatureList;



    private ControllerNeuroNetForm controller;



    public void runSimulation(Game2048 game2048) throws IOException {
        this.game2048 = game2048;
        this.controller = createNewStage();
        updateLabels();
        this.drawStart();
        this.creatureList = new Creature[POP_SIZE];

           for(int fiveSecondsWonder = 0; fiveSecondsWonder < this.creatureList.length; fiveSecondsWonder++) {
               this.creatureList[fiveSecondsWonder] = new Creature(game2048);
               try {
                   Neuronet.this.creatureList[Neuronet.this.currentCreature].
                           makeAction(Neuronet.this.creatureList[Neuronet.this.currentCreature].calculateMove());
               } catch (IOException e) {
                   System.out.println("Action wasn't calculated");
               }
               Neuronet.this.calculateFitness(game2048.getScore(), Neuronet.this.creatureList[currentCreature].getMaxTile());
               }
           this.calculateAverageFitness();
           this.averageFitLevels.add(Double.valueOf(this.averageFitness));

        Timeline mainTimeline = new Timeline(new KeyFrame(Duration.seconds(TICK_RATE), event -> {
            Neuronet.this.drawStart();

            Neuronet.this.currentCreature = Neuronet.this.randomReproduce();
            try {
                Neuronet.this.creatureList[Neuronet.this.currentCreature].
                        makeAction(Neuronet.this.creatureList[Neuronet.this.currentCreature].calculateMove());
            } catch (IOException ex) {
                System.out.println();
            }
            Neuronet.this.calculateFitness(Neuronet.this.game2048.getScore(), Neuronet.this.creatureList[currentCreature].getMaxTile());
            Neuronet.this.creatureList[Neuronet.this.currentCreature].draw(Neuronet.this.controller);
            Neuronet.this.fitLevels.add(Neuronet.this.creatureList[Neuronet.this.currentCreature].getFitness());
            Neuronet.this.reproductionCount++;
            Neuronet.this.calculateAverageFitness();
            if (Neuronet.this.reproductionCount % 50 == 0) {
                Neuronet.this.averageFitLevels.add(Neuronet.this.averageFitness);
            }
            if (Neuronet.this.reproductionCount % Neuronet.this.creatureList.length == 0) {
                Neuronet.this.generationCount++;
            }
            Neuronet.this.creatureList[Neuronet.this.currentCreature].draw(Neuronet.this.controller);
            Neuronet.this.updateLabels();
        }));

        mainTimeline.setCycleCount(-1);
        mainTimeline.play();

   }

    private ControllerNeuroNetForm createNewStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/NeuroNetForm.fxml"));
        Parent parent = loader.load();
        ControllerNeuroNetForm ct = loader.getController();
        Scene scene = new Scene(parent);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        return ct;
    }




    private void calculateFitness(int score, int maxTile) {
        if(score > bestScore)
            bestScore = score;
        double fit = score * SCORE_WEIGHT + maxTile * MAX_TILE_WEIGHT;
        this.creatureList[currentCreature].setFitness(fit);
    }

    private void updateLabels() {
        controller.updateTotalPopLabel(POP_SIZE);
        controller.updateAvgPopFittLabel(averageFitness);
        controller.updateNewCreatureLabel(currentCreature);
        controller.updateChildOfLabel("Child of: " + parent1 + " and " + parent2);
        if(currentCreature == 0){
            controller.updateFitnessLabel(0.0D);
        }else {
            controller.updateFitnessLabel(creatureList[currentCreature].getFitness());
        }
        controller.updateGenNumLabel(generationCount);
        controller.updateTotalMutLabel(mutations);
        controller.updateTotalReproductionLabel(reproductionCount);

    }



    private int randomReproduce() {
        Creature[] selection = new Creature[3];
        int[] numbers = new int[3];

        for (int i = 0; i < selection.length; ++i) {
            numbers[i] = (int)(Math.random() * (double) POP_SIZE);
            selection[i] = this.creatureList[numbers[i]];
        }

        for (int i = 1; i < selection.length; i++) {
            if (selection[i].getFitness() > selection[0].getFitness()) {
                Creature temp = selection[i];
                int rand = numbers[i];
                selection[i] = selection[0];
                numbers[i] = numbers[0];
                selection[0] = temp;
                numbers[0] = rand;
            }
        }

            this.parent1 = numbers[1];
            this.parent2 = numbers[2];

            Synapse[] newSynapses = new Synapse[20];

            for (int i = 0; i < newSynapses.length; ++i) {
                double rand = Math.random();
                newSynapses[i] = rand < 0.5D ? selection[1].getSynapse(i) : selection[2].getSynapse(i);

                if (newSynapses[i].mutate()) {
                    ++this.mutations;
                }
            }
                this.creatureList[numbers[0]] = new Creature(this.game2048, newSynapses);
                return numbers[0];
            }



    public Neuronet(int hiddenNodesCount, int popSize, float tickRate, ScreensController myController) {
        //default initializations

        POP_SIZE = popSize;
        TICK_RATE = tickRate;
        HIDDEN_COUNT = hiddenNodesCount;
        this.screenChangerController = myController;
    }


    private void calculateAverageFitness(){
        double totalFitness = 0.0D;

        for (int i = 0; i < this.creatureList.length; i++) {
            totalFitness += this.creatureList[i].getFitness();
        }

        this.averageFitness = totalFitness / (double) this.creatureList.length;
    }


    public void drawStart()  {

        controller.setStrokeColor(Color.CHARTREUSE);

        for (int i = 0; i < this.fitLevels.size(); i++) {
            if (this.generationCount > 0 && i % POP_SIZE == 0) {
                controller.drawLines(800.0D + 400.0D / (double) this.fitLevels.size() * (double) i,
                        0.0D,
                        800.0D + 400.0D / (double) this.fitLevels.size() * (double) i,
                        300.0D,
                        1.0D);

            }
        }

        controller.setStrokeColor(Color.DARKGREY);

        for(int i = 0; i < this.fitLevels.size() - 1; ++i) {
            controller.drawLines(800.0D + 400.0D / (double)this.fitLevels.size() * (double)i,
                    (this.fitLevels.get(i)).doubleValue() / 2.0D,
                    800.0D + 400.0D / (double)this.fitLevels.size() * (double)(i + 1),
                    (this.fitLevels.get(i + 1)).doubleValue() / 2.0D,
                    1.0D);
        }

        controller.setStrokeColor(Color.DARKBLUE);

        for(int i = 0; i < this.averageFitLevels.size() - 1; ++i) {
            controller.drawLines(800.0D + 400.0D / (double)this.fitLevels.size() * (double)i * 50.0D,
                    (this.averageFitLevels.get(i)).doubleValue() / 2.0D,
                    800.0D + 400.0D / (double)this.fitLevels.size() * (double)(i + 1) * 50.0D,
                    (this.averageFitLevels.get(i + 1)).doubleValue() / 2.0D,
                    1.0D);
        }

        controller.setStrokeColor(Color.DARKGRAY);

        if(this.fitLevels.size() > 25) {
            for(int i = 0; i < 25; ++i) {
                controller.drawLines((double)(800 + (25 - i) * 16),
                        300.0D + (this.fitLevels.get(this.fitLevels.size() - i - 1)).doubleValue() / 2.0D,
                        (double)(800 + (25 - i - 1) * 16),
                        300.0D + this.fitLevels.get(this.fitLevels.size() - i - 2).doubleValue() / 2.0D,
                        1.0D);
            }
        }

        controller.setStrokeColor(Color.BLUE);
        controller.drawLines(800.0D, 300.0D + this.averageFitness / 2.0D, 1200.0D, 300.0D + this.averageFitness / 2.0D,
                1.0D);

        controller.setStrokeColor(Color.BLUE);
        controller.drawLines(800.0D, this.averageFitness / 2.0D, 1200.0D, this.averageFitness / 2.0D,
                1.0D);
        controller.drawLines(800.0D, 300.0D + this.averageFitness / 2.0D, 1200.0D, 300.0D + this.averageFitness / 2.0D,
                1.0D);
        controller.setStrokeColor(Color.BLACK);
        controller.drawLines(800.0D, 0.0D, 800.0D, 600.0D,
                1.0D);
        controller.drawLines(800.0D, 300.0D, 1200.0D, 300.0D,
                1.0D);
    }

}
