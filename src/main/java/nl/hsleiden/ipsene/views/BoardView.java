package nl.hsleiden.ipsene.views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Card;
import nl.hsleiden.ipsene.models.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class BoardView implements View {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  private final String RED = "#FF0000";
  private final String BLUE = "#0000FF";
  private final String GREEN = "#00FF00";
  private final String YELLOW = "#FFFF00";

  private Stage primaryStage;

  private static BoardView boardView;
  private Thread timerThread;

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

    CardType card0Type;
    int card0steps;
    CardType card1Type;
    int card1steps;
    CardType card2Type;
    int card2steps;
    CardType card3Type;
    int card3steps;
    CardType card4Type;
    int card4steps;

    // TODO: dit aansturen aan de hand van de model(ik weet niet hoe dit moet!)
    ArrayList<Card> deck;

    // TODO: hoe veel tijd er nog voor de zet over is, aansturen a.d.h.v firebase(ik weet niet hoe dit moet!)
    int timer = 60;

    // TODO: Welke ronde we nu in zitten in een coole integer!
    int roundNumber = 3;

    // TODO: de huidige speler die aan de beurt is hier doorgeven
    int turnPlayerNumber = 3;


    Rectangle statRect = ViewHelper.createUIDividers(250, 700);
    ViewHelper.setNodeCoordinates(statRect, 1350, 0);

    Rectangle cardRect = ViewHelper.createUIDividers(1350, 200);
    ViewHelper.setNodeCoordinates(cardRect, 0, 700);

    ImageView keezBoadLogo = ViewHelper.createLogo(null, 150);
    ViewHelper.setNodeCoordinates(keezBoadLogo,1350,725);

    ImageView gameBoard = ViewHelper.drawGameBoard(null);
    // No coordinates need to be set, as its always at 0,0

//    DEBUG: to draw pawns in all tiles for checking coordinates
    ArrayList<Node> temp = new ArrayList<>();
    for(int i = 1; i < 101; i++){
      Polygon test = ViewHelper.createPawn(BLUE);
      ViewHelper.setPawnPosition(test, i);
      temp.add(test);
    }

    //RIGHT SIDEBAR
    Label timerHeader = ViewHelper.headerLabelBuilder("Time left:");
    ViewHelper.setNodeCoordinates(timerHeader, 1400, 10);

    Label timerLabel = new Label();
    timerLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
    timerLabel.setText(String.valueOf(timer));
    CountdownTimer countdownTimer = new CountdownTimer(timerLabel, timer, 1400, 20);
    this.timerThread = new Thread(countdownTimer);
    timerThread.setDaemon(true);
    timerThread.start();

    Label playersTurnDisplay = ViewHelper.playersTurnDisplay(turnPlayerNumber);
    ViewHelper.setNodeCoordinates(playersTurnDisplay, 1350, 200);

    Label roundNumberHeader = ViewHelper.headerLabelBuilder("Round number:");
    ViewHelper.setNodeCoordinates(roundNumberHeader, 1375, 280);

    Label roundNumberDisplay = ViewHelper.roundNumberDisplayBuilder(roundNumber, 1);
    ViewHelper.setNodeCoordinates(roundNumberDisplay, 1400,300);

    //BOTTOM CARD BAR
    VBox cardsText = ViewHelper.verticalTextDisplayBuilder("CARDS");
    ViewHelper.setNodeCoordinates(cardsText, 10, 700);

    ImageView testcard = ViewHelper.showCard(CardType.STEP_N, 6);
    ViewHelper.setNodeCoordinates(testcard, 50, 705);

    pane.getChildren().addAll(gameBoard ,statRect, cardRect, keezBoadLogo, timerLabel, timerHeader, playersTurnDisplay);
    pane.getChildren().addAll(cardsText, roundNumberDisplay, roundNumberHeader, testcard);
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
    try {
      timerThread.interrupt();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    loadPrimaryStage(createInitialPane());
  }
}
