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

public class GameStatView implements View {

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
    gameStatController.registerObserver(this);
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Scene scene = new Scene(pane, WIDTH, HEIGHT);
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

  private Pane createUpdatedPane() {
    Pane pane = new Pane();
    Text timeAsText = new Text("########");
    String currentTurnTimeStr = String.valueOf(gameStatController.getCurrentTurnTime());
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
      new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
          gameStatController.startTurnTimer();
        }
      };

  @Override
  public void update() throws FileNotFoundException {
    loadPrimaryStage(createUpdatedPane());
  }
}
