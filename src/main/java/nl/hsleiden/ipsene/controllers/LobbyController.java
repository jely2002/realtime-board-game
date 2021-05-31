package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import nl.hsleiden.ipsene.models.Lobby;
import nl.hsleiden.ipsene.views.Menu;
import nl.hsleiden.ipsene.views.View;

public class LobbyController implements Controller {

  static LobbyController lobbyController;
  Lobby lobby;

  private LobbyController() {
    lobby = new Lobby();
  }

  public static LobbyController getInstance() {
    if (lobbyController == null) {
      lobbyController = new LobbyController();
    }
    return lobbyController;
  }

  public void registerObserver(Menu menu) {
    lobby.registerObserver(menu);
  }

  public void quitGame() {
    Platform.exit();
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
