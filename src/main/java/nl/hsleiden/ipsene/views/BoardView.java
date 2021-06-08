package nl.hsleiden.ipsene.views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.interfaces.View;

import java.awt.*;
import java.util.ArrayList;

public class BoardView implements View {

  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  private final String RED = "#FF0000";
  private final String BLUE = "#0000FF";
  private final String GREEN = "#00FF00";
  private final String YELLOW = "#FFFF00";

  private Stage primaryStage;

  private static BoardView boardView;

  BoardController boardController;

  public BoardView(Stage s) {
    primaryStage = s;
    loadPrimaryStage(createInitialPane());
    this.boardController = new BoardController(4);
    boardController.registerObserver(this);
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Scene scene = new Scene(pane, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezboard");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Pane createInitialPane() {
    Pane pane = new Pane();

    Rectangle statRect = ViewHelper.createUIDividers(250, 700);
    ViewHelper.setNodeCoordinates(statRect, 1350, 0);

    Rectangle cardRect = ViewHelper.createUIDividers(1350, 200);
    ViewHelper.setNodeCoordinates(cardRect, 0, 700);

    Image logo = null;
    ImageView keezBoadLogo = ViewHelper.createLogo(logo, 150);
    ViewHelper.setNodeCoordinates(keezBoadLogo,1350,725);

    Image board = null;
    ImageView gameBoard = ViewHelper.drawGameBoard(board);
    // No coordinates need to be set, as its always at 0,0

//    DEBUG: to draw pawns in all tiles for checking coordinates
    ArrayList<Node> temp = new ArrayList<>();
    for(int i = 1; i < 101; i++){
      Polygon test = ViewHelper.createPawn(GREEN);
      ViewHelper.setPawnPosition(test, i);
      temp.add(test);
    }

    pane.getChildren().addAll(gameBoard ,statRect, cardRect, keezBoadLogo);
    pane.getChildren().addAll(temp);
    return pane;
  }


  EventHandler<MouseEvent> timerStartButtonClicked =
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
          boardController.startTurnTimer();
        }
      };

  @Override
  public void update() {
    loadPrimaryStage(createInitialPane());
  }
}
