package nl.hsleiden.ipsene.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class ViewHelper {
    /**
     * @param node The JavaFX node you want to position
     * @param x Target X Coordinate for your node
     * @param y Target Y Coordinate for your node
     */
    public static void setNodeCoordinates(Node node, int x, int y) {
        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    /**
     * @param node The JavaFX node you want to apply a drop shadow to
     */
    public static void applyDropShadow(Node node) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(2);
        shadow.setOffsetX(1);
        shadow.setOffsetX(1);
        node.setEffect(shadow);
    }
}
