package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import nl.hsleiden.ipsene.models.Menu;
import nl.hsleiden.ipsene.views.MenuView;

public class MenuController {

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

  public void registerObserver(MenuView menuView) {
    menu.registerObserver(menuView);
  }

  public void quitGame() {
    Platform.exit();
  }

  public void update(DocumentSnapshot ds) {

  }
}
