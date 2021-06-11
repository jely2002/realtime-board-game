package nl.hsleiden.ipsene.views;

import com.sun.javafx.geom.Vec2d;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.controllers.GameController;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardView implements View {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public static final int CARD_START_X_POSITION = 50;
  public static final int CARD_SEPERATION_VALUE = 130;
  private int lastCardX = CARD_START_X_POSITION;
  private boolean cardSelected = false;

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
  private GameController gameController;

  public BoardView(Stage s, GameController gameController) {
    primaryStage = s;
    this.gameController = gameController;
    this.boardController = new BoardController(4);
    boardController.registerObserver(this);
    gameController.registerObserver(this);
    gameController.getOwnPlayer().registerObserver(this);
    loadPrimaryStage(createInitialPane());
  }

  private void loadPrimaryStage(Pane pane) {
    logger.info("BoardView started!");
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
    // TODO: hoe veel tijd er nog voor de zet over is, aansturen a.d.h.v firebase(ik weet niet hoe
    // dit moet!)
    int timer = 60;

    // TODO: Welke ronde we nu in zitten in een coole integer!
    int roundNumber = 3;

    // TODO: de huidige speler die aan de beurt is hier doorgeven
    int turnPlayerNumber = 3;

    Rectangle statRect = ViewHelper.createUIDividers(250, 700);
    ViewHelper.setNodeCoordinates(statRect, 1350, 0);

    Rectangle cardRect = ViewHelper.createUIDividers(1350, 200);
    ViewHelper.setNodeCoordinates(cardRect, 0, 700);

    ImageView keezBoardLogo = ViewHelper.createLogo(null, 150);
    ViewHelper.setNodeCoordinates(keezBoardLogo, 1350, 725);

    ImageView gameBoard = ViewHelper.drawGameBoard();

    // pawns and cards
    ArrayList<Node> pawns = buildPawns();
    ArrayList<ImageView> cards = buildCards();

    // RIGHT SIDEBAR
    Label timerHeader = ViewHelper.headerLabelBuilder("Time left:");
    ViewHelper.setNodeCoordinates(timerHeader, 1400, 10);

    // the timer
    Label timerLabel = new Label();
    timerLabel.setStyle(
        "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
    timerLabel.setText(String.valueOf(timer));
    CountdownTimer countdownTimer = new CountdownTimer(timerLabel, timer, 1400, 20);
    this.timerThread = new Thread(countdownTimer);
    timerThread.setDaemon(true);
    timerThread.start();

    Label playersTurnDisplay = ViewHelper.playersTurnDisplay(turnPlayerNumber);
    ViewHelper.setNodeCoordinates(playersTurnDisplay, 1350, 200);

    //    Label roundNumberHeader = ViewHelper.headerLabelBuilder("Round number:");
    //    ViewHelper.setNodeCoordinates(roundNumberHeader, 1375, 280);

    VBox roundNumberDisplay = ViewHelper.roundNumberDisplayBuilder(roundNumber, 1);
    ViewHelper.setNodeCoordinates(roundNumberDisplay, 1375, 280);

    // BOTTOM CARD BAR
    VBox cardsText = ViewHelper.verticalTextDisplayBuilder("CARDS");
    ViewHelper.setNodeCoordinates(cardsText, 10, 700);

    pane.getChildren()
        .addAll(
            gameBoard,
            statRect,
            cardRect,
            keezBoardLogo,
            timerLabel,
            timerHeader,
            playersTurnDisplay);
    pane.getChildren().addAll(cardsText, roundNumberDisplay);
    pane.getChildren().addAll(pawns);
    pane.getChildren().addAll(cards);

    return pane;
  }

  /**
   * gets all pawns in the game and builds polygons for them
   *
   * @return a list of all the pawnlygons
   */
  private ArrayList<Node> buildPawns() {
    Player ourPlayer = gameController.getOwnPlayer();
    // -1 for the player number to player index
    ArrayList<Node> allpawns = new ArrayList<>();
    for (Team t : gameController.getTeams()) {
      for (int i = 0; i < Team.PLAYERS_PER_TEAM; i++) {
        Player p = t.getPlayer(i);
        for (final Pawn pawn : p.getPawns()) {
          Polygon poly = ViewHelper.createPawn(pawn.getTeamType().getCode());
          ViewHelper.setPawnPosition(poly, pawn.getBoardPosition());
          // only add event when this is one of our pawns
          if (p.equals(ourPlayer)) {
            poly.addEventFilter(MouseEvent.MOUSE_CLICKED, pawnClickedEvent);
          }
          allpawns.add(poly);
        }
      }
    }
    return allpawns;
  }

  /**
   * gets the owning player and builds image views from its cards
   *
   * @return a list of ImageViews representing cards
   */
  private ArrayList<ImageView> buildCards() {
    // show all our players cards
    cardSelected = false;
    Player ourPlayer = gameController.getOwnPlayer();
    ArrayList<ImageView> cards = new ArrayList<>();
    for (Card card : ourPlayer.getCards()) {
      ImageView cardview = ViewHelper.showCard(card.getType(), card.steps);
      cardview.addEventFilter(MouseEvent.MOUSE_CLICKED, cardClicked);
      ViewHelper.setNodeCoordinates(cardview, lastCardX, 705);
      cards.add(cardview);
      lastCardX += CARD_SEPERATION_VALUE;
    }
    return cards;
  }

  EventHandler<MouseEvent> timerStartButtonClicked =
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
          boardController.startTurnTimer();
        }
      };
  /**
   * called when one of our cards is clicked, calculates what card was clicked through its position
   */
  EventHandler<MouseEvent> cardClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          // todo check if this is our turn
          double mousex = mouseEvent.getSceneX();
          // get the index of the card we clicked on
          int clickedCardIndex = (int) ((mousex - CARD_START_X_POSITION) / CARD_SEPERATION_VALUE);
          Player ourPlayer = gameController.getOwnPlayer();
          if (clickedCardIndex < ourPlayer.getCards().size()) {
            ourPlayer.setSelectedCardIndex(clickedCardIndex);
            cardSelected = true;
          }
        }
      };
  /**
   * called when a pawn is clicked, checks if a card is selected, if it is calculates the pawn
   * closest to the clicked position. then sets that pawn as the selected pawn for the player and
   * calls doTurn and serializes the game again
   */
  EventHandler<MouseEvent> pawnClickedEvent =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (cardSelected) {
            Player ourPlayer = gameController.getOwnPlayer();
            Pawn closestPawn = ourPlayer.getPawn(0);
            // get the closest pawn to our click position
            for (int i = 1; i < Team.PAWNS_PER_PLAYER; i++) {
              Pawn p = ourPlayer.getPawn(i);
              double closestPawnDistance =
                  getPawnDistanceFromMouse(
                      closestPawn, mouseEvent.getSceneX(), mouseEvent.getSceneY());
              double pawnDistance =
                  getPawnDistanceFromMouse(p, mouseEvent.getSceneX(), mouseEvent.getSceneY());
              closestPawn = (closestPawnDistance < pawnDistance) ? closestPawn : p;
            }
            // do turn and sent to firebase
            ourPlayer.setSelectedPawnIndex(closestPawn.getPawnNumber());
            ourPlayer.doTurn();
            gameController.serialize();
          }
        }
      };

  /**
   * gets the distance between a pawn and a position on screen
   *
   * @param p the pawn to check for
   * @param mousex the mouse x position
   * @param mousey the mouse y position
   * @return the distance between the pawn and the mouse
   */
  private double getPawnDistanceFromMouse(Pawn p, double mousex, double mousey) {
    Vec2d realPos = ViewHelper.getRealPositionFromBoard(p.getBoardPosition());
    return Math.sqrt(Math.pow(realPos.x - mousex, 2) + Math.pow(realPos.y - mousey, 2));
  }

  @Override
  public void update() {
    // reset the x position of the cards to draw them anew
    lastCardX = CARD_START_X_POSITION;
    Platform.runLater(() -> loadPrimaryStage(createInitialPane()));
  }
}
