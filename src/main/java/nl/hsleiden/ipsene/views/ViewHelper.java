package nl.hsleiden.ipsene.views;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.models.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewHelper {
    private static final ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();
    private static final String RED = "#FF0000";
    private static final String BLUE = "#0000FF";
    private static final String GREEN = "#00FF00";
    private static final String YELLOW = "#FFFF00";
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    /**
     * Set location of JavaFX node by passing x and y coordinates
     *
     * @param node The JavaFX node you want to position
     * @param x Target X Coordinate for your node
     * @param y Target Y Coordinate for your node
     */
    public static void setNodeCoordinates(Node node, int x, int y) {
        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    /**
     * Apply a JavaFX drop shadow effect to a node
     *
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

    /**
     * Creates a KeezBoard Logo JavaFX Node
     *
     * @param img Pass a null Image object
     * @param fitHeight Desired height of the logo
     * @return Returns an ImageView Node of the logo
     */
    public static ImageView createLogo(Image img, int fitHeight){

        try {
            img = new Image(new FileInputStream("keez.png"));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(fitHeight);
        ViewHelper.applyDropShadow(imageView);

        return imageView;
    }

    /**
     * Creates a UI Divider for the GameBoard View to seperate UI elements
     *
     * @param width Width of the Divider in pixels
     * @param height Height of the Divider in pixels
     * @return Returns a rectangle Node with specified width and height
     */
    public static Rectangle createUIDividers(int width, int height){
        Rectangle rect = new Rectangle();

        rect.setHeight(height);
        rect.setWidth(width);
        rect.setStyle("-fx-fill: grey; -fx-stroke: black; -fx-stroke-width: 5;" +
                "");
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }

    /**
     * Creates a label formatted as a header
     *
     * @param txt Text in the label
     * @return returns label formatted as header with text
     */
    public static Label headerLabelBuilder(String txt) {
        Label lbl = new Label();

        lbl.setPrefWidth(580);
        lbl.setPrefHeight(50);
        lbl.setText(txt);
        lbl.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 30; -fx-text-fill: #000000");

        return lbl;
    }

    /**
     * Creates a label to display the player who's turn it currently is with appropriate formatting
     *
     * @param player player number who's turn it is currently
     * @return returns label with formatted player number and color
     */
    public static Label playersTurnDisplay(int player){
        String playerColor = null;
        String playerText = "Player ";
        String suffix = "'s turn";

        Label lbl = new Label();

        try {
            switch (player) {
                case 0 -> {
                    playerColor = RED;
                    playerText += "1" + suffix;
                }
                case 1 -> {
                    playerColor = BLUE;
                    playerText += "2" + suffix;
                }
                case 2 -> {
                    playerColor = GREEN;
                    playerText += "3" + suffix;
                }
                case 3 -> {
                    playerColor = YELLOW;
                    playerText += "4" + suffix;
                }
                default -> throw new IllegalStateException("Unexpected value: " + player);
            }
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e);
        }
        lbl.setText(playerText);
        lbl.setStyle("" +
                " -fx-font-family: 'Comic Sans MS';" +
                " -fx-text-fill: #000000;" +
                " -fx-background-color: grey;" +
                " -fx-font-size: 30;" +
                " -fx-label-padding: 15;" +
                " -fx-border-width: 5;" +
                " -fx-border-color: " + playerColor + ";");
        lbl.setMaxWidth(250);
        lbl.setMinWidth(250);

        return lbl;
    }

    /**
     * Creates a label to display the round number in a more human-readable format with appropriate styling
     *
     * @param roundNumber the current round number
     * @param startOffset set to 0 if round counting starts at 1, set to 0 if counting starts at 1
     * @return returns label with formatted round number
     */
    public static Label roundNumberDisplayBuilder(int roundNumber, int startOffset){
        String display;
        roundNumber = roundNumber + startOffset;

        Label lbl = new Label();
        display = ((roundNumber / 3) + 1) + "." + roundNumber % 3;
        lbl.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
        lbl.setText(display);

        return lbl;
    }

    /**
     * Creates the vertical text display for the CARDS text in the gameboard
     *
     * @param text Text to make vertically displayed
     * @return returns vbox with vertical text
     */
    public static VBox verticalTextDisplayBuilder(String text){
        String[] chars = text.split("(?!^)");
        ArrayList<String> charsAsStrings = new ArrayList<>();
        Collections.addAll(charsAsStrings, chars);

        VBox vbx = new VBox();

        for (String charsAsString : charsAsStrings) {
            Text txt = new Text();
            txt.setText(charsAsString);
            txt.setStyle(" -fx-text-fill: #000000;" +
                    " -fx-font-size: 27.5;" +
                    " -fx-font-family: 'Comic Sans MS';");
            vbx.getChildren().add(txt);
        }

        return vbx;
    }

    /**
     * Draws the game board for the Boardview class
     *
     * @param img Pass a null Image object
     * @return returns ImageView with gameboard image.
     */
    public static ImageView drawGameBoard(Image img){
        try {
            img = new Image(new FileInputStream("board.png"));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        fillCoordinateList();

        return imageView;
    }

    public static ImageView showCard(CardType type, int steps){
        String path = "src/main/resources/assets/cards/";
        Image img = null;

        switch (type){
            case SPAWN_STEP_1 -> path += "spawnor1.png";
            case SPAWN -> path += "spawn.png";
            case SUB -> path += "trade.png";
            case STEP_4 -> path += "4.png";
            case STEP_7 -> path += "7.png";
            case STEP_N -> {
                switch (steps){
                    case 2 -> path += "2.png";
                    case 3 -> path += "3.png";
                    case 5 -> path += "5.png";
                    case 6 -> path += "6.png";
                    case 8 -> path += "8.png";
                    case 9 -> path += "9.png";
                    case 10 -> path += "10.png";
                    case 12 -> path += "12.png";
                    default -> throw new IllegalStateException("Unexpected value: " + steps);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        try {
            img = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(190);
        return imageView;
    }

    /**
     * Creates a JavaFX Polygon in the shape of a pawn
     *
     * @param color Infill color of the pawn
     * @return returns polygon with specified infill color in the shape of a pawn
     */
    public static Polygon createPawn(String color){
        Polygon poly = new Polygon();

        poly.getPoints().addAll(
                5.0, 0.0,
                15.0, 0.0,
                17.5, 2.5,
                17.5, 5.0,
                15.0, 7.5,
                15.0, 10.0,
                20.0, 22.0,
                20.0, 25.0,
                0.0, 25.0,
                0.0, 22.0,
                5.0, 7.5,
                2.4, 2.5,
                2.4, 5.0);
        poly.setStyle("-fx-fill: " + color + "; -fx-stroke: black; -fx-stroke-width: 1;");
        poly.setStrokeType(StrokeType.INSIDE);

        return poly;
    }

    /**
     * Fills the Coordinate Arraylist with coordinates for pawn tiles
     */
    private static void fillCoordinateList(){
        setIndexCoordinates(0, 0, 0);
        setIndexCoordinates(1, 252, 33);
        setIndexCoordinates(2, 292, 33);
        setIndexCoordinates(3, 252, 71);
        setIndexCoordinates(4, 292, 71);
        setIndexCoordinates(5, 338, 128);
        setIndexCoordinates(6, 339, 165);
        setIndexCoordinates(7, 337, 202);
        setIndexCoordinates(8, 339, 240);
        setIndexCoordinates(9, 330, 275);
        setIndexCoordinates(10, 937, 40);
        setIndexCoordinates(11, 977, 40);
        setIndexCoordinates(12, 937, 75);
        setIndexCoordinates(13, 977, 75);
        setIndexCoordinates(14, 918, 151);
        setIndexCoordinates(15, 880, 160);
        setIndexCoordinates(16, 848, 185);
        setIndexCoordinates(17, 813, 160);
        setIndexCoordinates(18, 775, 181);
        setIndexCoordinates(19, 1006, 606);
        setIndexCoordinates(20, 1043, 606);
        setIndexCoordinates(21, 1006, 643);
        setIndexCoordinates(22, 1046, 643);
        setIndexCoordinates(23, 913, 658);
        setIndexCoordinates(24, 882, 629);
        setIndexCoordinates(25, 855, 597);
        setIndexCoordinates(26, 855, 557);
        setIndexCoordinates(27, 855, 517);
        setIndexCoordinates(28, 235, 629);
        setIndexCoordinates(29, 273, 629);
        setIndexCoordinates(30, 234, 666);
        setIndexCoordinates(31, 273, 666);
        setIndexCoordinates(32, 348, 606);
        setIndexCoordinates(33, 362, 571);
        setIndexCoordinates(34, 368, 535);
        setIndexCoordinates(35, 368, 503);
        setIndexCoordinates(36, 368, 465);
        setIndexCoordinates(37, 374, 118);
        setIndexCoordinates(38, 413, 116);
        setIndexCoordinates(39, 451, 117);
        setIndexCoordinates(40, 478, 143);
        setIndexCoordinates(41, 513, 163);
        setIndexCoordinates(42, 547, 145);
        setIndexCoordinates(43, 590, 134);
        setIndexCoordinates(44, 628, 122);
        setIndexCoordinates(45, 662, 142);
        setIndexCoordinates(46, 697, 121);
        setIndexCoordinates(47, 734, 114);
        setIndexCoordinates(48, 778, 114);
        setIndexCoordinates(49, 818, 105);
        setIndexCoordinates(50, 860, 100);
        setIndexCoordinates(51, 893, 119);
        setIndexCoordinates(52, 944, 186);
        setIndexCoordinates(53, 917, 212);
        setIndexCoordinates(54, 947, 235);
        setIndexCoordinates(55, 916, 262);
        setIndexCoordinates(56, 949, 290);
        setIndexCoordinates(57, 921, 318);
        setIndexCoordinates(58, 940, 349);
        setIndexCoordinates(59, 941, 389);
        setIndexCoordinates(60, 911, 418);
        setIndexCoordinates(61, 910, 455);
        setIndexCoordinates(62, 938, 484);
        setIndexCoordinates(63, 904, 506);
        setIndexCoordinates(64, 907, 544);
        setIndexCoordinates(65, 930, 572);
        setIndexCoordinates(66, 962, 592);
        setIndexCoordinates(67, 934, 614);
        setIndexCoordinates(68, 959, 640);
        setIndexCoordinates(69, 867, 665);
        setIndexCoordinates(70, 839, 638);
        setIndexCoordinates(71, 808, 665);
        setIndexCoordinates(72, 771, 646);
        setIndexCoordinates(73, 746, 618);
        setIndexCoordinates(74, 713, 639);
        setIndexCoordinates(75, 674, 631);
        setIndexCoordinates(76, 636, 632);
        setIndexCoordinates(77, 597, 623);
        setIndexCoordinates(78, 558, 611);
        setIndexCoordinates(79, 529, 641);
        setIndexCoordinates(80, 493, 621);
        setIndexCoordinates(81, 456, 639);
        setIndexCoordinates(82, 422, 617);
        setIndexCoordinates(83, 382, 622);
        setIndexCoordinates(84, 313, 591);
        setIndexCoordinates(85, 287, 564);
        setIndexCoordinates(86, 318, 542);
        setIndexCoordinates(87, 289, 521);
        setIndexCoordinates(88, 262, 494);
        setIndexCoordinates(89, 259, 458);
        setIndexCoordinates(90, 295, 434);
        setIndexCoordinates(91, 267, 406);
        setIndexCoordinates(92, 268, 369);
        setIndexCoordinates(93, 295, 340);
        setIndexCoordinates(94, 296, 300);
        setIndexCoordinates(95, 274, 271);
        setIndexCoordinates(96, 305, 240);
        setIndexCoordinates(97, 271, 215);
        setIndexCoordinates(98, 302, 186);
        setIndexCoordinates(99, 271, 162);
        setIndexCoordinates(100, 300, 135);
    }

    /**
     * Sets the x and y coordinates of the specified index in the coordinates ArrayList
     *
     * @param index Target Index of Coordinates ArrayList
     * @param x X coordinate
     * @param y Y coordinate
     */
    private static void setIndexCoordinates(int index, int x, int y){
        ArrayList<Integer> coords = new ArrayList<>();
        coords.add(x);
        coords.add(y);
        coordinates.add(index, coords);
    }

    /**
     * Returns the X coordinate of the specified index in the coordinates ArrayList
     *
     * @param index Target Index of Coordinates ArrayList
     * @return returns X coordinate integer
     */
    private static int getCoordinateMapX(int index){
        ArrayList<Integer> coords = coordinates.get(index);
        return coords.get(0);
    }

    /**
     * Returns the Y coordinate of the specified index in the coordinates ArrayList
     *
     * @param index Target Index
     * @return returns Y coordinate integer
     */
    private static int getCoordinateMapY(int index){
        ArrayList<Integer> coords = coordinates.get(index);
        return coords.get(1);
    }

    /**
     * Sets a pawns location to the coordinates of an index in the Coordinates Arraylist
     *
     * @param pawn Pawn to set the location of
     * @param index Target index of Coordinates ArrayList
     */
    public static void setPawnPosition(Polygon pawn, int index){
        setNodeCoordinates(pawn, getCoordinateMapX(index), getCoordinateMapY(index));
    }
}
