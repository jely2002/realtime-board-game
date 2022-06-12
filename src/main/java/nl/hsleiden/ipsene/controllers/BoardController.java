package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import com.sun.javafx.PlatformUtil;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Board;
import nl.hsleiden.ipsene.models.enums.PlayerColour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardController implements Controller {

  private static final Logger logger = LoggerFactory.getLogger(BoardController.class.getName());

  static BoardController boardController;
  private final Board board;

  private BoardController() {
    this.board = Board.getInstance();
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {
    board.registerObserver(v);
  }

  public void unregisterObserver(View v) {
    board.unregisterObserver(v);
  }

  public static BoardController getInstance() {
    if (boardController == null) {
      boardController = new BoardController();
    }
    return boardController;
  }

  /** @return null if the game has not been won */
  public PlayerColour hasGameBeenWon() {
    return board.hasTheGameBeenWon();
  }

  public static void openWebpage(String url) {
    try {
      if (PlatformUtil.isWindows()) {
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
      }
      if (PlatformUtil.isLinux()) {
        Runtime.getRuntime().exec("xdg-open " + url);
      }
      if (PlatformUtil.isMac()) {
        Runtime.getRuntime().exec("open " + url);
      }
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
    }
  }
}
