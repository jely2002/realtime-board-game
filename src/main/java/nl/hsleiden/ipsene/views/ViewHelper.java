package nl.hsleiden.ipsene.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import nl.hsleiden.ipsene.application.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewHelper {
    private static ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

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

    public static Rectangle createUIDividers(int width, int height){
        Rectangle rect = new Rectangle();

        rect.setHeight(height);
        rect.setWidth(width);
        rect.setStyle("-fx-fill: grey; -fx-stroke: black; -fx-stroke-width: 5;" +
                "");
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }

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
    private static void setIndexCoordinates(int index, int x, int y){
        ArrayList<Integer> coords = new ArrayList<>();
        coords.add(x);
        coords.add(y);
        coordinates.add(index, coords);
    }
    private static int getCoordinateMapX(int index){
        ArrayList<Integer> coords = coordinates.get(index);
        return coords.get(0);
    }
    private static int getCoordinateMapY(int index){
        ArrayList<Integer> coords = coordinates.get(index);
        return coords.get(1);
    }

    public static void setPawnPosition(Node pawn, int index){
        setNodeCoordinates(pawn, getCoordinateMapX(index), getCoordinateMapY(index));
    }
}
