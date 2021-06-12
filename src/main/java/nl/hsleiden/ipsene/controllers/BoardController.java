package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Board;
import nl.hsleiden.ipsene.models.Deck;

public class BoardController implements Controller {

  static BoardController boardController;
  private final Board board;

  private Deck cards;
  private final boolean gameHasEnded = false;

  public BoardController() {
    this.board = new Board();
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {
    board.registerObserver(v);
  }

  public static BoardController getInstance() {
    if (boardController == null) {
      boardController = new BoardController();
    }
    return boardController;
  }

  /**
   * Returns the remaining amount of seconds of a turn from the model
   *
   * @return remaining amount of seconds
   */
  public int getCurrentTurnTime() {
    return board.getCurrentTurnTime();
  }

  /** Starts the turnTimer by calling the model */
  public void startTurnTimer() {
    board.startTurnTimer();
  }

  public static void openWebpage(String url) {
    try {
      java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
    } catch (java.io.IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
