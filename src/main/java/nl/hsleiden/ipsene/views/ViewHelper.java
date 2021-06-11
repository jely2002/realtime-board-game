package nl.hsleiden.ipsene.views;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.geom.Vec2d;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewHelper {
  private static final ArrayList<Vec2d> coordinates = new ArrayList<>();
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
  public static ImageView createLogo(Image img, int fitHeight) {

    try {
      img = new Image(loadResource("/assets/branding/keez.png"));
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
  public static Rectangle createUIDividers(int width, int height) {
    Rectangle rect = new Rectangle();

    rect.setHeight(height);
    rect.setWidth(width);
    rect.setStyle("-fx-fill: grey; -fx-stroke: black; -fx-stroke-width: 5;" + "");
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

  public static InputStream loadResource(String resourcePath) throws FileNotFoundException {
    InputStream resourceStream = ViewHelper.class.getResourceAsStream(resourcePath);
    if (resourceStream == null)
      throw new FileNotFoundException("The resource at " + resourcePath + " could not be found.");
    return resourceStream;
  }

  /**
   * Creates a label to display the player who's turn it currently is with appropriate formatting
   *
   * @param player player number who's turn it is currently
   * @return returns label with formatted player number and color
   */
  public static Label playersTurnDisplay(int player) {
    String playerColor = null;
    String playerText = "Player ";
    String suffix = "'s turn";

    Label lbl = new Label();

    try {
      switch (player) {
        case 0:
          {
            playerColor = RED;
            playerText += "1" + suffix;
            break;
          }
        case 1:
          {
            playerColor = BLUE;
            playerText += "2" + suffix;
            break;
          }
        case 2:
          {
            playerColor = GREEN;
            playerText += "3" + suffix;
            break;
          }
        case 3:
          {
            playerColor = YELLOW;
            playerText += "4" + suffix;
            break;
          }
        default:
          {
            throw new IllegalStateException("Unexpected value: " + player);
          }
      }
    } catch (IllegalStateException e) {
      logger.error(e.getMessage(), e);
    }
    lbl.setText(playerText);
    lbl.setStyle(
        ""
            + " -fx-font-family: 'Comic Sans MS';"
            + " -fx-text-fill: #000000;"
            + " -fx-background-color: grey;"
            + " -fx-font-size: 30;"
            + " -fx-label-padding: 15;"
            + " -fx-border-width: 5;"
            + " -fx-border-color: "
            + playerColor
            + ";");
    lbl.setMaxWidth(250);
    lbl.setMinWidth(250);

    return lbl;
  }

  /**
   * Creates a label to display the round number in a more human-readable format with appropriate
   * styling
   *
   * @param roundNumber the current round number
   * @param startOffset set to 0 if round counting starts at 1, set to 0 if counting starts at 1
   * @return returns label with formatted round number
   */
  public static VBox roundNumberDisplayBuilder(int roundNumber, int startOffset) {
    String css = "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000";
    roundNumber = roundNumber + startOffset;

    VBox vbx = new VBox();
    Label roundNumberHeader = ViewHelper.headerLabelBuilder("Round Number:");


    Label roundNumberLabel = new Label();
    roundNumberLabel.setStyle(css);
    roundNumberLabel.setText(String.valueOf(((roundNumber / 3) + 1)));
    roundNumberLabel.setTranslateX(55);

    Label subroundNumberHeader = ViewHelper.headerLabelBuilder("Sub Round:");

    Label subroundNumberLabel = new Label();
    subroundNumberLabel.setStyle(css);
    subroundNumberLabel.setText(String.valueOf(roundNumber % 3));
    subroundNumberLabel.setTranslateX(55);

    vbx.getChildren().addAll(roundNumberHeader, roundNumberLabel, subroundNumberHeader, subroundNumberLabel);

    return vbx;
  }

  /**
   * Creates the vertical text display for the CARDS text in the gameboard
   *
   * @param text Text to make vertically displayed
   * @return returns vbox with vertical text
   */
  public static VBox verticalTextDisplayBuilder(String text) {
    String[] chars = text.split("(?!^)");
    ArrayList<String> charsAsStrings = new ArrayList<>();
    Collections.addAll(charsAsStrings, chars);

    VBox vbx = new VBox();

    for (String charsAsString : charsAsStrings) {
      Text txt = new Text();
      txt.setText(charsAsString);
      txt.setStyle(
          " -fx-text-fill: #000000;"
              + " -fx-font-size: 27.5;"
              + " -fx-font-family: 'Comic Sans MS';");
      vbx.getChildren().add(txt);
    }

    return vbx;
  }

  /**
   * Draws the game board for the Boardview class
   *
   * @return returns ImageView with gameboard image.
   */
  public static ImageView drawGameBoard() {
    Image img = null;
    try {
      img = new Image(loadResource("/assets/board/board.png"));
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage(), e);
    }
    ImageView imageView = new ImageView(img);
    imageView.setPreserveRatio(true);
    try {
      fillCoordinateList();
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage(), e);
    }

    return imageView;
  }

  public static ImageView showCard(CardType type, int steps) {
    String path = "/assets/cards/";
    Image img = null;
    path += type.getCardBackground(steps);
    try {
      img = new Image(loadResource(path));
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
  public static Polygon createPawn(String color) {
    Polygon poly = new Polygon();

    poly.getPoints()
        .addAll(
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

  /** Fills the Coordinate Arraylist with coordinates for pawn tiles */
  private static void fillCoordinateList() throws FileNotFoundException {
    Reader reader = new InputStreamReader(loadResource("/assets/board/positions.json"));
    Type listType = new TypeToken<ArrayList<Vec2d>>() {}.getType();
    List<Vec2d> points = new Gson().fromJson(reader, listType);
    for (int i = 0; i < points.size(); i++) {
      Vec2d point = points.get(i);
      coordinates.add(i, point);
    }
  }

  /**
   * Returns the X coordinate of the specified index in the coordinates ArrayList
   *
   * @param index Target Index of Coordinates ArrayList
   * @return returns X coordinate integer
   */
  private static int getCoordinateMapX(int index) {
    Vec2d point = coordinates.get(index);
    return (int) point.x;
  }

  /**
   * Returns the Y coordinate of the specified index in the coordinates ArrayList
   *
   * @param index Target Index
   * @return returns Y coordinate integer
   */
  private static int getCoordinateMapY(int index) {
    Vec2d point = coordinates.get(index);
    return (int) point.y;
  }

  /**
   * Sets a pawns location to the coordinates of an index in the Coordinates Arraylist
   *
   * @param pawn Pawn to set the location of
   * @param index Target index of Coordinates ArrayList
   */
  public static void setPawnPosition(Polygon pawn, int index) {
    setNodeCoordinates(pawn, getCoordinateMapX(index), getCoordinateMapY(index));
  }

  public static Vec2d getRealPositionFromBoard(int index) {
    return coordinates.get(index);
  }
}
