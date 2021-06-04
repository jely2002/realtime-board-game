package nl.hsleiden.ipsene.application;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.BoardController;
import nl.hsleiden.ipsene.models.*;
import nl.hsleiden.ipsene.views.Menu;

public class GameController extends Application {

  public static void run(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Menu menu = new Menu(primaryStage);
    BoardController boardController = new BoardController(4, 2);
    boardController.doGameLoop();
  }
}
