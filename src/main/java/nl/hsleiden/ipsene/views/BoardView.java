package nl.hsleiden.ipsene.views;

import com.sun.javafx.geom.Vec2d;
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
import nl.hsleiden.ipsene.controllers.GameController;
import nl.hsleiden.ipsene.interfaces.View;

import nl.hsleiden.ipsene.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
    subscribeToPawns(gameController);
    loadPrimaryStage(createInitialPane());
  }
  private void subscribeToPawns(GameController controller) {
    // we are going in a loopedieloop...
    for (Team t : controller.getGame().getTeams()) {
      for (int i = 0; i < Team.PLAYERS_PER_TEAM; i++) {
        for (int j = 0; j < Team.PAWNS_PER_PLAYER; j++) {
          t.getPawn(i, j).registerObserver(this);
        }
      }
    }
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

    ImageView keezBoardLogo = ViewHelper.createLogo(null, 150);
    ViewHelper.setNodeCoordinates(keezBoardLogo,1350,725);

    ImageView gameBoard = ViewHelper.drawGameBoard();
    // No coordinates need to be set, as its always at 0,0

    ArrayList<Node> pawns = buildPawns();

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

    ArrayList<ImageView> cards = buildCards();

    //BOTTOM CARD BAR
    VBox cardsText = ViewHelper.verticalTextDisplayBuilder("CARDS");
    ViewHelper.setNodeCoordinates(cardsText, 10, 700);

    pane.getChildren().addAll(gameBoard ,statRect, cardRect, keezBoardLogo, timerLabel, timerHeader, playersTurnDisplay);
    pane.getChildren().addAll(cardsText, roundNumberDisplay, roundNumberHeader);
    pane.getChildren().addAll(pawns);
    pane.getChildren().addAll(cards);

    return pane;
  }
  private ArrayList<Node> buildPawns() {
    Game g = gameController.getGame();
    int ourPlayersIndex = g.getOwnPlayer();
    // -1 for the player number to player index
    ArrayList<Node> allpawns = new ArrayList<>();
    for (Team t : gameController.getGame().getTeams()) {
      for (int i = 0; i < Team.PLAYERS_PER_TEAM; i++) {
        Player p = t.getPlayer(i);
        for(final Pawn pawn : p.getPawns()) {
          Polygon poly = ViewHelper.createPawn(pawn.getTeamType().getCode());
          ViewHelper.setPawnPosition(poly, pawn.getBoardPosition());
          // only add event when this is one of our pawns
          if (p.equels(g.getPlayer(ourPlayersIndex))) {
            poly.addEventFilter(MouseEvent.MOUSE_CLICKED, pawnClickedEvent);
          }
          allpawns.add(poly);
        }
      }
    }
    return allpawns;
  }
  private ArrayList<ImageView> buildCards() {
    // show all our players cards
    cardSelected = false;
    Game g = gameController.getGame();
    // -1 for the player number to player index
    int p = g.getOwnPlayer() - 1;
    Player ourPlayer = g.getPlayer(p);
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
  EventHandler<MouseEvent> cardClicked = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent mouseEvent) {
      // todo check if this is our turn
      double mousex = mouseEvent.getSceneX();
      // get the index of the card we clicked on
      int clickedCardIndex = (int) ((mousex - CARD_START_X_POSITION) / CARD_SEPERATION_VALUE);
      gameController.getGame().getPlayer(gameController.getGame().getOwnPlayer()).setSelectedCardIndex(clickedCardIndex);
      cardSelected = true;
    }
  };
  EventHandler<MouseEvent> pawnClickedEvent = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent mouseEvent) {
      if (cardSelected) {
        Player ourPlayer = gameController.getGame().getPlayer(gameController.getGame().getOwnPlayer());
        Pawn closestPawn = ourPlayer.getPawn(0);
        // get the closest pawn to our click position
        for (int i = 1; i < Team.PAWNS_PER_PLAYER; i++) {
          Pawn p = ourPlayer.getPawn(i);
          double closestPawnDistance = getPawnDistanceFromMouse(closestPawn, mouseEvent.getSceneX(), mouseEvent.getSceneY());
          double pawnDistance = getPawnDistanceFromMouse(p, mouseEvent.getSceneX(), mouseEvent.getSceneY());
          closestPawn = (closestPawnDistance < pawnDistance) ? closestPawn : p;
        }
        ourPlayer.setSelectedPawnIndex(closestPawn.getPawnNumber());
        ourPlayer.doTurn();
      }
    }
  };
  private double getPawnDistanceFromMouse(Pawn p, double mousex, double mousey) {
    Vec2d realPos = ViewHelper.getRealPositionFromBoard(p.getBoardPosition());
    return Math.sqrt(Math.pow(realPos.x - mousex, 2) + Math.pow(realPos.y - mousey, 2));
  }
  @Override
  public void update() {
    try {
      //timerThread.interrupt();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    lastCardX = CARD_START_X_POSITION;
    loadPrimaryStage(createInitialPane());
  }

}
