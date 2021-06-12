package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import java.io.IOException;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;

public class VictoryController implements Controller {

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {

  }

  public void backToMainMenu(Stage primaryStage, FirebaseService firebaseService)
      throws IOException {
    LobbyController lobbyController = new LobbyController(firebaseService, primaryStage);
  }
}
