package nl.hsleiden.ipsene.application;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.CardController;
import nl.hsleiden.ipsene.models.*;
import nl.hsleiden.ipsene.views.AccountView;
import nl.hsleiden.ipsene.views.LobbyView;

public class Main extends Application {

  public static void run(String[] args) {
//    CardController c = new CardController(CardController.generateDeck(4));
//    for (int i = 0; i < 52; i++) {
//      Card card = c.drawCard();
//      card.play(new Player(), new Pawn(TeamType.GREEN,0));
//    }

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    LobbyView b = new LobbyView(primaryStage);
  }
}
