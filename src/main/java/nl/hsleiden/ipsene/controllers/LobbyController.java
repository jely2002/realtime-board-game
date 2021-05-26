package nl.hsleiden.ipsene.controllers;

import javafx.application.Platform;
import nl.hsleiden.ipsene.models.Lobby;
import nl.hsleiden.ipsene.views.LobbyView;

public class LobbyController {

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

  public void registerObserver(LobbyView lobbyView) {
    lobby.register(lobbyView);
  }

  public void quitGame() {
    Platform.exit();
  }
}
