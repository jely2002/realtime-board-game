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
import nl.hsleiden.ipsene.models.PlayerColour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VictoryView implements View {
  private static final Logger logger = LoggerFactory.getLogger(MenuView.class.getName());

  private final Stage primaryStage;
  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  private final FirebaseService firebaseService;

  private final VictoryController victoryController = new VictoryController();

  private static final Effect frostEffect = new BoxBlur(50, 50, 3);

  private static VictoryView victoryView = null;

  public static VictoryView getInstance(Stage primaryStage, FirebaseService firebaseService) {
    if (victoryView == null) {
      return new VictoryView(primaryStage, firebaseService);
    } else return victoryView;
  }
  /** @param primaryStage give primary stage */
  private VictoryView(Stage primaryStage, FirebaseService firebaseService) {
    this.primaryStage = primaryStage;
    this.firebaseService = firebaseService;
  }

  public void show(PlayerColour winner) {
    Platform.runLater(() -> loadPrimaryStage(createPane(winner)));
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Pane root = pane;
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezbord");
      primaryStage.show();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private Pane createPane(PlayerColour winningTeam) {
    Pane pane = new Pane();

    ImageView backgroundLogo = ViewHelper.createLogo(null, 900);
    backgroundLogo.setEffect(frostEffect);
    // Position does not need to be set, as its supposed to be at 0,0

    Rectangle textBackdrop = ViewHelper.createUIDividers(600, 300);
    ViewHelper.setNodeCoordinates(textBackdrop, 500, 300);

    Label winnerLabel = ViewHelper.winnerLabelBuilder(true, winningTeam.toString());
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
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
          logger.debug("back to menu button clicked");
          try {
            victoryController.backToMainMenu(primaryStage, firebaseService);
          } catch (IOException e) {
            logger.error(e.getMessage(), e);
          }
        }
      };

  @Override
  public void update() {}
}
