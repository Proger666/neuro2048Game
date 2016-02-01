package neuronet;

import controllers.ControllerNeuroNetForm;
import javafx.scene.paint.Color;

/**
 * Created by Scorpa on 12/18/2015.
 */

  public class Point {

        //Draw neuroNode
        private Color col;
    protected double signal;

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getSignal() { return signal;}
    private double xPos;
        private double yPos;

        public Point(double x, double y) {
            this(x, y, Color.rgb((int)(Math.random() * 255.0D), (int)(Math.random() * 255.0D), (int)(Math.random() * 255.0D)));
        }

        public Point(double x, double y, Color c) {
            this.xPos = x;
            this.yPos = y;
            this.col = c;
        }
        public void draw(ControllerNeuroNetForm controllerNeuroNetForm) {
            controllerNeuroNetForm.setStrokeColor(Color.BLACK);
            controllerNeuroNetForm.drawNodes(col,this.xPos,this.yPos,50.0D,50.0D);
        }
        public void setColor(int r, int g, int b) {
            this.col = Color.rgb(r, g, b);
        }

    public void setSignal(int inputValue){ this.signal = inputValue; }

    public void calculateSignal(Synapse[] synapses) {
        double output = 0.0D;

        for(int i = 0; i < synapses.length; ++i) {

            if (synapses[i].getDestination().equals(this))
                output += synapses[i].getSource().getSignal() * synapses[i].getWeight();


        }
        this.signal = sigmoid(output);
    }
    private double sigmoid(double x){return  2/(1+Math.exp(-4.9*x))-1;}

    public boolean equals(Point p){
        if(this.xPos == p.getxPos() && this.yPos == p.getyPos())
            return true;
        return false;
    }




}
