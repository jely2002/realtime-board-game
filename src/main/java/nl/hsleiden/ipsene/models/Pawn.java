package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Pawn implements FirebaseSerializable<Map<String, Object>>, Model {
  private final Game game;
  private int boardPosition;
  private final PlayerColour colour;
  private Player player;
  private int pawnNumber;
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
  public void setIsInsideEndPool(boolean val) { isInsideVictoryPool = val; }
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
        //System.out.println("wrapped around diff: " + diff);
      }
      else {
        boardPosition = pos;
      }
      //System.out.println("added: " + pos + " new pos: " + boardPosition);

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
        if (Board.isInEndPosition(colour, boardPosition + i)) {
          Board.putPawnIntoEndPool(colour, this);
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

  @Override
  public void update(DocumentSnapshot document) {}

  public void update(int position) {
    this.setBoardPosition(position);
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
