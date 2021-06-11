package nl.hsleiden.ipsene.application;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.LobbyController;
import nl.hsleiden.ipsene.firebase.FirebaseService;
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
          new FirebaseService("/firestoretest-5c4e4-52601abc4d0c.json", "games");
      LobbyController lobbyController = new LobbyController(firebaseService, primaryStage);

      // todo gamecontroller would normally go through LobbyController#startGame
      // GameController gameController = new GameController(firebaseService, game);
      // gameController.join("20999");
      // BoardView brd = new BoardView(primaryStage, gameController);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
