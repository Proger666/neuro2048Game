package neuronet;

import controllers.ControllerNeuroNetForm;
import javafx.scene.paint.Color;

/**
 * Created by Scorpa on 12/19/2015.
 */
public class Synapse {
    private static final double MUTATE_CHANSE = 0.005D;
    private double startx;
    private double starty;
    private double endx;
    private double endy;
    private double weight;
    private Point source;
    private Point destination;


    public Synapse(double sx, double sy, double ex, double ey, Point s, Point d) {
        this(sx, sy, ex, ey, s, d, Math.random());
    }

    public Synapse(double sx, double sy, double ex, double ey, Point s , Point d, double w) {
        this.startx = sx;
        this.endx = ex;
        this.starty = sy;
        this.endy = ey;
        this.weight = w;
        this.source = s;
        this.destination = d;
    }

    public void draw(ControllerNeuroNetForm controllerNeuroNetForm) {
        controllerNeuroNetForm.setStrokeColor(Color.GREY);
        controllerNeuroNetForm.drawLines(this.startx, this.starty, this.endx, this.endy, 10.0D * this.weight);
    }

    public Point getSource() {
        return this.source;
    }
    public Point getDestination() { return this.destination; }

    public double getWeight() {
        return this.weight;
    }

    public boolean mutate() {
        double chance = Math.random();
        if(chance < MUTATE_CHANSE) {
            double direction = Math.random();
            double amount = Math.random() * 0.05D;
            if(direction < 0.5D) {
                this.weight += amount;
            } else {
                this.weight -= amount;
                if(this.weight < 0.0D) {
                    this.weight = 0.0D;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
