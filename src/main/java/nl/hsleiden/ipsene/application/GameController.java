package nl.hsleiden.ipsene.application;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.TeamController;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.models.Game;
import nl.hsleiden.ipsene.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

public class GameController extends Application {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class.getName());

  public static void run(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    //testFirebase();

    // GameStatView gameStatView = new GameStatView(primaryStage);
    // logger.debug("primaryStage has been loaded");
    // Menu menu = new Menu(primaryStage);
    // CardView cardView = new CardView(primaryStage);
    // LobbyView lobbyView = new LobbyView(primaryStage);
    //     BoardController boardController = new BoardController(4, 2);
    //     boardController.doGameLoop();

  }

  private void testFirebase() {
    Game game = new Game();
    logger.info(game.serialize().toString());
    try {
      FirebaseService firebaseService = new FirebaseService("C:\\Users\\jwgle\\Downloads\\firestoretest-5c4e4-52601abc4d0c.json", "games");
      firebaseService.set(game.getId().toString(), game.serialize());
    } catch(IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

}
