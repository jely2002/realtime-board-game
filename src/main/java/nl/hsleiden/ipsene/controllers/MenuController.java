package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import nl.hsleiden.ipsene.models.Menu;
import nl.hsleiden.ipsene.views.MenuView;
import nl.hsleiden.ipsene.views.View;

public class MenuController implements Controller {

  static MenuController lobbyController;
  Menu menu;

  private MenuController() {
    menu = new Menu();
  }

  public static MenuController getInstance() {
    if (lobbyController == null) {
      lobbyController = new MenuController();
    }
    return lobbyController;
  }

  public void quitGame() {
    Platform.exit();
  }

  public void update(DocumentSnapshot ds) {

  }

  @Override
  public void registerObserver(View v) {
    menu.registerObserver(v);
  }
}
