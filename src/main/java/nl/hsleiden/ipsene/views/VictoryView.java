package nl.hsleiden.ipsene.views;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.VictoryController;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VictoryView implements View {
  private static final Logger logger = LoggerFactory.getLogger(MenuView.class.getName());
  private final Stage primaryStage;
  private final int WIDTH = 1600;
  private final int HEIGHT = 900;
  private final boolean playerHasWon;
  private final int winningTeam;

  private final FirebaseService firebaseService;

  private final VictoryController victoryController = new VictoryController();

  private static final Effect frostEffect = new BoxBlur(50, 50, 3);

  private static VictoryView victoryView = null;
  public static VictoryView getInstance(Stage primaryStage) {
    if (victoryView == null) {
      return new VictoryView(primaryStage, null, false, -1);
    }
    else return victoryView;
  }
  /**
   * @param primaryStage give primary stage
   * @param playerHasWon if the player('s team) has won the game or not
   * @param winningTeam team number of the winning team
   */
  private VictoryView(
      Stage primaryStage, FirebaseService firebaseService, boolean playerHasWon, int winningTeam) {
    this.primaryStage = primaryStage;
    this.playerHasWon = playerHasWon;
    this.winningTeam = winningTeam;
    this.firebaseService = firebaseService;

  }

  private void show() {
    loadPrimaryStage(createPane());
  }
  private void loadPrimaryStage(Pane pane) {
    try {
      Pane root = pane;
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezbord");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Pane createPane() {
    Pane pane = new Pane();

    ImageView backgroundLogo = ViewHelper.createLogo(null, 900);
    backgroundLogo.setEffect(frostEffect);
    // Position does not need to be set, as its supposed to be at 0,0

    Rectangle textBackdrop = ViewHelper.createUIDividers(600, 300);
    ViewHelper.setNodeCoordinates(textBackdrop, 500, 300);

    Label winnerLabel = ViewHelper.winnerLabelBuilder(playerHasWon, winningTeam);
    winnerLabel.layout();

    Platform.runLater(
        () -> {
          int labelWidth = (int) winnerLabel.getWidth();
          ViewHelper.setNodeCoordinates(winnerLabel, ((600 - labelWidth) / 2 + 500), 430);
        });

    Button menuButton = ViewHelper.buttonBuilder("EXIT TO MENU");
    ViewHelper.setNodeCoordinates(menuButton, 700, 700);
    menuButton.addEventFilter(MouseEvent.MOUSE_CLICKED, quitButtonClicked);

    pane.getChildren().addAll(backgroundLogo, textBackdrop, winnerLabel, menuButton);

    return pane;
  }

  EventHandler<MouseEvent> quitButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          logger.debug("Back to menu button clicked");
          try {
            victoryController.backToMainMenu(primaryStage, firebaseService);
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        }
      };

  @Override
  public void update() {
    loadPrimaryStage(createPane());
  }
}
