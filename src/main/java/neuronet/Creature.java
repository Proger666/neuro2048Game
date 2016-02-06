package neuronet;



import game2048.Game2048;
import controllers.ControllerNeuroNetForm;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Scorpa on 12/18/2015.
 */
public class Creature {
    private final  Game2048 game2048;
    private final Canvas scene;

    //Static Variables
    List<String> keys = new LinkedList<String>() {{
        add("UP");
        add("DOWN");
        add("RIGHT");
        add("LEFT");
    }};

    //Points for this creature
    private Point[] points;
    private HiddenNode[] hiddenNodes;
    private OutputNode[] outputNodes;
    private InputNode[] inputNodes;

    //Synapses for this creature
    private List<Synapse> creatureSynapses;

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    private double fitness = 0.0D;
    private Synapse[] synapses;



    public Creature(Game2048 game2048, Canvas scene) {
        this.game2048 = game2048;
        this.scene = scene;

        this.createPoints();
        this.createSynapses(Neuronet.HIDDEN_COUNT * Neuronet.INPUTS_COUNT + Neuronet.HIDDEN_COUNT * Neuronet.OUTPUTS_COUNT);
        this.setSignals();

    }

    public Creature(Game2048 game2048, Synapse[] s, Canvas scene) {
        this.game2048 = game2048;
        this.scene = scene;

        this.createPoints();
        this.synapses = s;
        this.setSignals();
    }

    protected String calculateMove() throws FileNotFoundException {
        double maxSignal = 0;
        OutputNode decNode = null;
        for (OutputNode o :
                this.outputNodes) {
            if(o.signal > maxSignal) {
                maxSignal = o.signal;
                decNode = o;
            }
        }

        decNode.setColor(255,255,0);
        return decNode.getKey();
    }

    private void createPoints() {
        int totalPoints = Neuronet.INPUTS_COUNT + Neuronet.HIDDEN_COUNT + Neuronet.OUTPUTS_COUNT;
        this.points = new Point[totalPoints];
        this.inputNodes = new InputNode[Neuronet.INPUTS_COUNT];
        this.hiddenNodes = new HiddenNode[Neuronet.HIDDEN_COUNT];
        this.outputNodes = new OutputNode[Neuronet.OUTPUTS_COUNT];


        int curPoint = 0;
        for (int in = 0; in < Neuronet.INPUTS_COUNT; in++, curPoint++) {
            this.inputNodes[in] = new InputNode(scene.getWidth() * 0.05D, getYlocation(in, inputNodes.length) , Color.PALEVIOLETRED);
            points[curPoint] = inputNodes[in];
        }

        for (int hn = 0; hn < Neuronet.HIDDEN_COUNT; hn++, curPoint++) {

            points[curPoint] = this.hiddenNodes[hn] =
                    new HiddenNode(scene.getWidth() * 0.20D, getYlocation(hn, hiddenNodes.length) , Color.ALICEBLUE);

        }

        int i = 0;
        for (int out = 0; out < Neuronet.OUTPUTS_COUNT; out++, curPoint++) {
            points[curPoint] = this.outputNodes[out] =
                    new OutputNode(scene.getWidth() * 0.40D, getYlocation(out, outputNodes.length) , Color.GREEN);

            this.outputNodes[out].setKey(keys.get(i++));
        }

    }

    private double getYlocation(int v, int c) {
        double result = (float) 100 / (c + 1) / 100;
        result = (scene.getHeight() - 20.0D) - (scene.getHeight() * result * (v + 1));

        return  result;
    }

    public void setSignals() {
        try {
            int i = 0;
            for (Game2048.Tile t :
                    game2048.getTiles()) {
                inputNodes[i++].signal = 0.0D + t.getValue();
            }
        }catch (NullPointerException ex) {
            System.out.println();
        }
            for (HiddenNode h :
                    this.hiddenNodes) {
                h.calculateSignal(synapses);
            }
            for (OutputNode o :
                    this.outputNodes) {
                o.calculateSignal(synapses);
            }

    }

    private void createSynapses(int totalSynapses) {

        this.synapses = new Synapse[totalSynapses];

        int tS = 0;
        for (InputNode in :
                inputNodes) {
            for (int i = 0; i < Neuronet.HIDDEN_COUNT; i++, tS++) {
                this.synapses[tS] = new Synapse(in.getxPos() + 25.0D, in.getyPos() + 25.0D,
                        hiddenNodes[i].getxPos() + 25.0D, hiddenNodes[i].getyPos() + 25.0D, in, hiddenNodes[i]);
            }
        }
        for (HiddenNode hn :
                hiddenNodes) {

            for (int i = 0; i < Neuronet.OUTPUTS_COUNT; i++, tS++) {
                this.synapses[tS] = new Synapse(hn.getxPos() + 25.0D, hn.getyPos() + 25.0D,
                        outputNodes[i].getxPos() + 25.0D, outputNodes[i].getyPos() + 25.0D, hn, outputNodes[i]);
            }
        }
    }

    public Synapse getSynapse(int i) {
        return this.synapses[i];
    }

    public void draw(ControllerNeuroNetForm controller) {
        for (int i = 0; i < synapses.length; i++) {
            this.synapses[i].draw(controller);
        }

        for (int i = 0; i < points.length; i++) {
            this.points[i].draw(controller);
        }

    }

    public int getMaxTile() {
        int maxTileValue = 0;

        for (Game2048.Tile t :
                game2048.getTiles()) {
            if (t.getValue() > maxTileValue) {
                maxTileValue = t.getValue();
            }
        }
        return maxTileValue;
    }

    public void makeAction(String action) throws IOException {
        if (game2048.canMove()) {
            switch (action) {
                case "UP":
                    this.game2048.up();
                    break;
                case "DOWN":
                    this.game2048.down();
                    break;
                case "RIGHT":
                    this.game2048.right();
                    break;
                case "LEFT":
                    this.game2048.left();
                    break;
                default:
                    throw new IOException();
            }
        }
    }
}
