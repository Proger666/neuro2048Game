package neuronet;

import javafx.scene.paint.Color;


/**
 * Created by Scorpa on 12/19/2015.
 */
public class OutputNode extends HiddenNode {

    private String key;

    public OutputNode(double x, double y) {
        super(x, y);
    }

    public OutputNode(double x, double y, Color c) {
        super(x, y, c);
    }

    public void setKey(String key) { this.key = key;}

    public String getKey() { return key;}


}
