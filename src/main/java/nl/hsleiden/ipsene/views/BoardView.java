package nl.hsleiden.ipsene.views;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import nl.hsleiden.ipsene.models.Card;
import nl.hsleiden.ipsene.models.Pawn;
import nl.hsleiden.ipsene.models.PlayerColour;
import nl.hsleiden.ipsene.models.Team;
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

  private final Stage primaryStage;

  private static BoardView boardView;
  private Thread timerThread;

  BoardController boardController;
  private final GameController gameController;
  private VictoryView victoryView;

  public BoardView(Stage s, GameController gameController) {
    primaryStage = s;
    victoryView = VictoryView.getInstance(s, gameController.getFirebaseService());
    this.gameController = gameController;
    this.boardController = BoardController.getInstance();
    boardController.registerObserver(this);
    gameController.registerObserver(this);
    loadPrimaryStage(createInitialPane());
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Scene scene = new Scene(pane, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezboard");
      primaryStage.show();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private Pane createInitialPane() {
    lastCardX = CARD_START_X_POSITION;

    Pane pane = new Pane();
    int timer = gameController.getTimeLeft();
    int roundNumber = gameController.getRound();
    int turnPlayerNumber = gameController.getIdCurrentPlayer();

    Rectangle statRect = ViewHelper.createUIDividers(250, 700);
    ViewHelper.setNodeCoordinates(statRect, 1350, 0);

    Rectangle cardRect = ViewHelper.createUIDividers(1350, 200);
    ViewHelper.setNodeCoordinates(cardRect, 0, 700);

    ImageView keezBoardLogo = ViewHelper.createLogo(150);
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
    CountdownTimer countdownTimer = new CountdownTimer(gameController, timerLabel, timer, 1400, 20);
    this.timerThread = new Thread(countdownTimer);
    timerThread.setDaemon(true);
    timerThread.start();

    Label playersTurnDisplay = ViewHelper.playersTurnDisplay(turnPlayerNumber);
    ViewHelper.setNodeCoordinates(playersTurnDisplay, 1350, 200);

    VBox roundNumberDisplay = ViewHelper.roundNumberDisplayBuilder(roundNumber, 1);
    ViewHelper.setNodeCoordinates(roundNumberDisplay, 1375, 280);

    // BOTTOM CARD BAR
    VBox cardsText = ViewHelper.verticalTextDisplayBuilder("CARDS");
    ViewHelper.setNodeCoordinates(cardsText, 10, 700);

    // menu buttons
    Button gameRulesButton = buildMenuButton("RULES");
    ViewHelper.setNodeCoordinates(gameRulesButton, 5, 5);
    gameRulesButton.addEventFilter(MouseEvent.MOUSE_CLICKED, openGameRulesButtonClicked);

    Button returnToMainMenuButton = buildMenuButton("EXIT");
    ViewHelper.setNodeCoordinates(returnToMainMenuButton, 85, 5);
    returnToMainMenuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, returnToMainMenuButtonClicked);

    // surrender button
    Button surrenderButton = buildSurrenderButton();
    surrenderButton.addEventFilter(MouseEvent.MOUSE_CLICKED, surrenderEvent);

    pane.getChildren()
        .addAll(
            gameBoard,
            statRect,
            cardRect,
            keezBoardLogo,
            timerLabel,
            timerHeader,
            playersTurnDisplay,
            surrenderButton);
    pane.getChildren().addAll(cardsText, roundNumberDisplay);
    pane.getChildren().addAll(gameRulesButton, returnToMainMenuButton);
    pane.getChildren().addAll(pawns);
    pane.getChildren().addAll(cards);

    return pane;
  }

  private Button buildSurrenderButton() {
    Button button = new Button();
    button.setText("Surrender");
    button.setPrefWidth(170);
    button.setPrefHeight(170);
    ViewHelper.setNodeCoordinates(button, 1165, 715);
    button.setStyle("-fx-font-size: 20; -fx-background-color: " + RED);
    ViewHelper.applyDropShadow(button);
    return button;
  }

  private Button buildMenuButton(String text) {
    Button button = new Button();

    button.setText(text);
    button.setPrefWidth(75);
    button.setPrefHeight(75);
    button.setStyle("-fx-font-size: 10;");

    return button;
  }

  EventHandler<MouseEvent> surrenderEvent =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (gameController.isOwnPlayerCurrentPlayer()) {
            gameController.passTurn();
            loadPrimaryStage(createInitialPane());
          }
        }
      };

  /**
   * gets all pawns in the game and builds polygons for them
   *
   * @return a list of all the pawnlygons
   */
  private ArrayList<Node> buildPawns() {
    // -1 for the player number to player index
    ArrayList<Node> allpawns = new ArrayList<>();
    for (final Team team : gameController.getTeams()) {
      for (int i = 0; i < Team.PLAYERS_PER_TEAM; i++) {
        for (final Pawn pawn : team.getPawnsFromPlayer(i)) {
          Polygon poly = ViewHelper.createPawn(pawn);
          ViewHelper.setPawnPosition(poly, pawn.getBoardPosition());
          // only add event when this is one of our pawns and it is our turn
          if (gameController.isPlayerOwnPlayer(team, i)
              && gameController.isOwnPlayerCurrentPlayer()) {
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
    ArrayList<ImageView> cards = new ArrayList<>();
    for (Card card : gameController.getOwnPlayerCards()) {
      ImageView cardview = ViewHelper.showCard(card.getType(), card.steps);
      cardview.addEventFilter(MouseEvent.MOUSE_CLICKED, cardClicked);
      int ycoordinate = (card.isSelected()) ? 740 : 705;
      ViewHelper.setNodeCoordinates(cardview, lastCardX, ycoordinate);
      cards.add(cardview);
      lastCardX += CARD_SEPERATION_VALUE;
    }
    return cards;
  }

  /**
   * called when one of our cards is clicked, calculates what card was clicked through its position
   */
  EventHandler<MouseEvent> cardClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (gameController.isOwnPlayerCurrentPlayer()) {
            double mousex = mouseEvent.getSceneX();
            // get the index of the card we clicked on
            int clickedCardIndex = (int) ((mousex - CARD_START_X_POSITION) / CARD_SEPERATION_VALUE);
            if (gameController.setOwnPlayerClickedCardIndex(clickedCardIndex)) {
              cardSelected = true;
              loadPrimaryStage(createInitialPane());
            }
          }
        }
      };

  /**
   * called when a pawn is clicked, checks if a card is selected, if it is calculates the pawn
   * closest to the clicked position. then sets that pawn as the selected pawn for the player and
   * calls doTurn and serializes the game again
   */
  EventHandler<MouseEvent> pawnClickedEvent =
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          gameController.clickPawn(cardSelected, mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
      };

  EventHandler<MouseEvent> returnToMainMenuButtonClicked =
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
          logger.debug("Back to menu button clicked");
          gameController.backToMainMenu();
        }
      };
  EventHandler<MouseEvent> openGameRulesButtonClicked =
      e -> {
        logger.debug("gameRules webpage opened");
        BoardController.openWebpage("https://github.com/jely2002/IIPSENE/wiki/Rules");
      };

  @Override
  public void update() {
    PlayerColour potentialWinner = boardController.hasGameBeenWon();
    if (potentialWinner != null) {
      boardController.unregisterObserver(this);
      gameController.unregisterObserver(this);
      // someone has won the game
      victoryView.show(potentialWinner);
      gameController.removeGame();
    } else {
      Platform.runLater(() -> loadPrimaryStage(createInitialPane()));
      if (gameController.isOwnPlayerCurrentPlayer()) {
        if (gameController.hasOwnPlayerPassed()) {
          Platform.runLater(() -> gameController.passTurn());
        }
      }
    }
  }
}
