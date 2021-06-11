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
  public void registerObserver(View v) {}

  public void backToMainMenu(Stage primaryStage) throws IOException {
    FirebaseService firebaseService =
        new FirebaseService("/firestoretest-5c4e4-52601abc4d0c.json", "games");
    LobbyController lobbyController = new LobbyController(firebaseService, primaryStage);
  }
}
