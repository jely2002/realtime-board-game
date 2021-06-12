package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Pawn implements FirebaseSerializable<Map<String, Object>>, Model {
  private final Game game;
  private int boardPosition;
  private final PlayerColour colour;
  private Player player;
  private final int pawnNumber;
  private boolean isInsidePool = true;
  private boolean isInsideVictoryPool = false;

  /**
   * @param colour - the 'type' of the team, from enum TeamType used to get team info and calculate
   *     position
   * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board
   *     position
   */
  public Pawn(PlayerColour colour, int pawnNum, Game game) {
    this.game = game;
    this.colour = colour;
    boardPosition = Board.getFirstPoolPosition(colour) + pawnNum;
    pawnNumber = pawnNum;
  }

  public void setIsInsideEndPool(boolean val) {
    isInsideVictoryPool = val;
  }

  public PlayerColour getPlayerColour() {
    return colour;
  }

  public void setOwningPlayer(Player player) {
    this.player = player;
  }

  public int getPawnNumber() {
    return pawnNumber;
  }

  public int getBoardPosition() {
    return boardPosition;
  }

  public void setBoardPosition(int pos) {
    if (!isInsidePool && !isInsideVictoryPool) {
      // wrap around if necessary
      if (pos >= Board.HIGHEST_BOARD_POSITION) {
        int diff = Math.abs(pos - Board.HIGHEST_BOARD_POSITION);
        boardPosition = Board.START_POSITION_INDEX + diff;
      } else {
        boardPosition = pos;
      }

      notifyObservers();
    }
  }

  public void takeOutOfPool() {
    isInsidePool = false;
    setBoardPosition(Board.getFirstBoardPosition(colour));
  }

  public boolean isOutOfPool() {
    return !isInsidePool;
  }

  /** @param amount the amount of 'tiles' to add to the pawns position */
  public void addRelativeBoardPosition(int amount) {
    // dont move if pawn is still inside pool
    if (!isInsidePool || amount != 0) {
      for (int i = 0; i <= amount; i++) {
        if (Board.getInstance().isInEndPosition(colour, boardPosition + i)) {
          Board.getInstance().putPawnIntoEndPool(colour, this);
          break;
        }
      }
      setBoardPosition(boardPosition + amount);
    }
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serializedPawn = new LinkedHashMap<>();
    serializedPawn.put("location", getBoardPosition());
    serializedPawn.put("owner", player.getId());
    return serializedPawn;
  }

  /** DON'T use this method, it does nothing
   * use Pawn#update(int position) instead directly from the player for better performance
   * @param document the document received from firebase
   */
  @Override
  public void update(DocumentSnapshot document) { }

  /** sets the pawns position, should be used to update from firebase in the player
   * @param position the new position
   */
  public void update(int position) {
    boardPosition = position;
    if (Board.getInstance().isInsideEndPool(colour, position)) {
      Board.getInstance().putPawnIntoEndPool(colour, this);
    }
  }

  private final ArrayList<View> observers = new ArrayList<>();

  @Override
  public void registerObserver(View v) {
    this.observers.add(v);
  }

  @Override
  public void unregisterObserver(View v) {
    this.observers.remove(v);
  }

  @Override
  public void notifyObservers() {
    for (View v : observers) {
      v.update();
    }
  }
}
