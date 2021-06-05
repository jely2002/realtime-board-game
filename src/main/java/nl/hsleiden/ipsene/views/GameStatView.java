package nl.hsleiden.ipsene.views;

import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.GameStatController;
import nl.hsleiden.ipsene.models.GameStat;

public class GameStatView {

  private final int WIDTH = 200;
  private final int HEIGHT = 650;

  private Stage primaryStage;

  private static GameStatView gameStatView;

  GameStatController gameStatController;
  // GameStat gameStat;

  public GameStatView(Stage s) {
    primaryStage = s;
    loadPrimaryStage(createInitialPane());
    gameStatController = GameStatController.getInstance();
    // PASS IT TO THE CONTROLLER WHO WILL PASS IT TO THE MODEL
    gameStatController.registerObserver(this);
    // gameStatView = this;
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Pane root = pane;
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezbord-Stat");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Pane createInitialPane() {
    Pane pane = new Pane();
    Text timeAsText = new Text("########");
    timeAsText.setTranslateX(100);
    timeAsText.setTranslateY(50);

    Button timerStartButton = new Button("Start Timer");
    timerStartButton.setTranslateX(10);
    timerStartButton.setTranslateY(50);

    pane.getChildren().addAll(timeAsText, timerStartButton);

    timerStartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, timerStartButtonClicked);

    return pane;
  }

  private Pane createUpdatedPane(GameStat gameStat) {
    Pane pane = new Pane();
    Text timeAsText = new Text("########");
    String currentTurnTimeStr = String.valueOf(gameStat.getCurrentTurnTime());
    timeAsText.setText(currentTurnTimeStr);
    timeAsText.setTranslateX(100);
    timeAsText.setTranslateY(50);

    Button timerStartButton = new Button("Start Timer");
    timerStartButton.setTranslateX(10);
    timerStartButton.setTranslateY(50);

    pane.getChildren().addAll(timeAsText, timerStartButton);

    timerStartButton.addEventFilter(MouseEvent.MOUSE_CLICKED, timerStartButtonClicked);

    return pane;
  }

  public static GameStatView getInstance() {
    return gameStatView;
  }

  EventHandler<MouseEvent> timerStartButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          gameStatController.startTurnTimer();
        }
      };

  public void update(GameStat gamestat) throws FileNotFoundException {
    loadPrimaryStage(createUpdatedPane(gamestat));
  }
}
