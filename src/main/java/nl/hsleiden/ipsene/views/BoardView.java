package nl.hsleiden.ipsene.views;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.controllers.GameController;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardView implements View {

  private static final Logger logger = LoggerFactory.getLogger(BoardView.class.getName());

  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  private Stage primaryStage;

  private static BoardView boardView;

  BoardController boardController;
  private GameController gameController;

  public BoardView(Stage s, GameController gameController) {
    primaryStage = s;
    this.gameController = gameController;
    gameController.registerObserver(this);
    loadPrimaryStage((Pane) createInitialPane());
  }

  private void loadPrimaryStage(Pane pane) {
    logger.info("BoardView started!");
    try {
      Scene scene = new Scene(pane, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezbord-Game");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Object createInitialPane() {
    Pane pane = new Pane();

    return pane;
  }

  private Pane createUpdatedPane() {
    Pane pane = new Pane();

    return pane;
  }

  @Override
  public void update() {
    loadPrimaryStage(createUpdatedPane());
  }
}
