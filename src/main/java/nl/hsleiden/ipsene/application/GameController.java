package nl.hsleiden.ipsene.application;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.views.Menu;

public class GameController extends Application {

  public static void run(String[] args) {
    //    CardController c = new CardController(CardController.generateDeck(4));
    //    Card card = c.drawCard();
    //    card.play(new Player(), new Pawn(TeamType.GREEN,0));

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Menu menu = new Menu(primaryStage);
  }
}
