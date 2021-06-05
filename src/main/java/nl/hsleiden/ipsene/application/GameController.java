package nl.hsleiden.ipsene.application;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.views.GameStatView;
import nl.hsleiden.ipsene.views.LobbyView;

import java.io.FileNotFoundException;

public class GameController extends Application {

  public static void run(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    GameStatView gameStatView = new GameStatView(primaryStage);
//    try {
////    Menu menu = new Menu(primaryStage);
////    CardView cardView = new CardView(primaryStage);
//      //LobbyView lobbyView = new LobbyView(primaryStage);
//      // BoardController boardController = new BoardController(4, 2);
//      // boardController.doGameLoop();
//
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    }
  }
}
