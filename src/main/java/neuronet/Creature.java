package neuronet;



import game2048.Game2048;
import controllers.ControllerNeuroNetForm;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

/**
 * Created by Scorpa on 12/18/2015.
 */
public class Creature {
    private final  Game2048 game2048;

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



    public Creature(Game2048 game2048) {
        this.game2048 = game2048;
        this.createPoints();
        this.createSynapses(Neuronet.HIDDEN_COUNT * Neuronet.INPUTS_COUNT + Neuronet.HIDDEN_COUNT * Neuronet.OUTPUTS_COUNT);
        this.setSignals();

    }

    public Creature(Game2048 game2048, Synapse[] s) {
        this.game2048 = game2048;
        this.createPoints();
        this.synapses = s;
        this.setSignals();
    }

    protected String calculateMove(){
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


        int curPoint =0;
        for (int in = 0; in < Neuronet.INPUTS_COUNT; in++, curPoint++) {
            this.inputNodes[in] = new InputNode(125.0D, 85.0D + 25.0D * in, Color.RED);
            points[curPoint] = inputNodes[in];
        }

        for (int hn = 0; hn < Neuronet.HIDDEN_COUNT; hn++, curPoint++) {

            points[curPoint] = this.hiddenNodes[hn] =
                    new HiddenNode(375.0D, 85.0D + 25.0D * hn, Color.rgb(255, 0, 0));

        }

        int i = 0;
        for (int out = 0; out < Neuronet.OUTPUTS_COUNT; out++, curPoint++) {
            points[curPoint] = this.outputNodes[out] =
                    new OutputNode(375.0D, 85.0D + 25.0D * out, Color.rgb(255, 0, 0));

            this.outputNodes[out].setKey(keys.get(i++));
        }

    }

    private void setSignals() {
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
                this.synapses[tS] = new Synapse(in.getxPos(), in.getyPos(),
                        hiddenNodes[i].getxPos(), hiddenNodes[i].getyPos(), in, hiddenNodes[i]);
            }
        }
        for (HiddenNode hn :
                hiddenNodes) {

            for (int i = 0; i < Neuronet.OUTPUTS_COUNT; i++, tS++) {
                this.synapses[tS] = new Synapse(hn.getxPos(), hn.getyPos(),
                        outputNodes[i].getxPos(), outputNodes[i].getyPos(), hn, outputNodes[i]);
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

    public int getScore() {
        return game2048.getScore();
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
