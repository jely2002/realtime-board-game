package nl.hsleiden.ipsene.application;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.models.Menu;
import nl.hsleiden.ipsene.views.CardView;
import nl.hsleiden.ipsene.views.GameStatView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController extends Application {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class);

  public static void run(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    //GameStatView gameStatView = new GameStatView(primaryStage);
    //logger.debug("primaryStage has been loaded");
    //Menu menu = new Menu(primaryStage);
    //CardView cardView = new CardView(primaryStage);
    //LobbyView lobbyView = new LobbyView(primaryStage);
    //BoardController boardController = new BoardController(4, 2);
    //boardController.doGameLoop();

  }
}
