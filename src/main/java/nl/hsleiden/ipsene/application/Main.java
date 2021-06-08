package nl.hsleiden.ipsene.application;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.GameController;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.views.MenuView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public static void run(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    try {
      FirebaseService firebaseService =
          new FirebaseService(
              "C:\\Users\\jwgle\\Downloads\\firestoretest-5c4e4-52601abc4d0c.json", "games");
      GameController gameController = GameController.getInstance(firebaseService);
      MenuView menuView = new MenuView(primaryStage, gameController);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    // GameStatView gameStatView = new GameStatView(primaryStage);
    // logger.debug("primaryStage has been loaded");
    // Menu menu = new Menu(primaryStage);
    // CardView cardView = new CardView(primaryStage);
    // LobbyView lobbyView = new LobbyView(primaryStage);

    //     BoardController boardController = new BoardController(4, 2);
    //     boardController.doGameLoop();

  }
}
