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
  private boolean isHover = false;

  public boolean isHover() {
    return isHover;
  }

  public void setHover(boolean hover) {
    isHover = hover;
  }
  /**
   * @param colour - the 'type' of the team, from enum TeamType used to get team info and calculate
   *     position
   * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board
   *     position
   */
  public Pawn(PlayerColour colour, int pawnNum, Game game) {
    this.game = game;
    this.colour = colour;
    boardPosition = pawnNum;
    pawnNumber = pawnNum;
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
    if (isInsidePool) {
      return Board.getFirstPoolPosition(colour) + boardPosition + 1;
    }
    return Board.getFirstBoardPosition(colour) + boardPosition + 1;
  }

  public int getRelativeRealBoardPosition() {
    return boardPosition;
  }

  private void setRelativeBoardposition(int pos) {
    if (!isInsidePool) {
      boardPosition = pos;
      notifyObservers();
    }
  }

  public void takeOutOfPool() {
    isInsidePool = true;
    setRelativeBoardposition(Board.getFirstBoardPosition(getPlayerColour()));
  }

  public boolean isOutOfPool() {
    return !isInsidePool;
  }

  /** @param amount the amount of 'tiles' to add to the pawns position */
  public void addRelativeBoardPosition(int amount) {
    // if pos has changed then pawn is now outside pool
    if (isInsidePool && amount != 0) {
      isInsidePool = false;
      setRelativeBoardposition(amount);
      boardPosition = 0;
    }
    setRelativeBoardposition(boardPosition + amount);
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serializedPawn = new LinkedHashMap<>();
    serializedPawn.put("location", getRelativeRealBoardPosition());
    serializedPawn.put("owner", player.getId());
    return serializedPawn;
  }

  @Override
  public void update(DocumentSnapshot document) {}

  public void update(int position) {
    this.setRelativeBoardposition(position);
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
