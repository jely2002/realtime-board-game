package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Board;
import nl.hsleiden.ipsene.models.Deck;

public class BoardController implements Controller {

  static BoardController boardController = null;
  private Board board;

  private Deck cards;
  private TeamController teamController;
  private boolean gameHasEnded = false;
  private final int AMOUNT_OF_PLAYERS;

  public BoardController(int amount_of_players) {
    this.board = new Board();
    AMOUNT_OF_PLAYERS = amount_of_players;
  }

  public static void getInstance() {

  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {
    board.registerObserver(v);
  }

  public static BoardController getInstance(int amount_of_players) {
    if (boardController == null) {
      boardController = new BoardController(amount_of_players);
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
}
