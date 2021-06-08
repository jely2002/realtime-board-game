package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;

public class Pawn implements FirebaseSerializable<Map<String, Object>> {
  private final Game game;
  private int boardPosition;
  private final TeamType team;
  private Player player;
  private int pawnNumber;

  /**
   * @param team - the 'type' of the team, from enum TeamType used to get team info and calculate
   *     position
   * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board
   *     position
   */
  public Pawn(TeamType team, int pawnNum, Game game) {
    this.game = game;
    this.team = team;
    boardPosition = pawnNum;
    pawnNumber = pawnNum;
  }

  public void setOwningPlayer(Player player) {
    this.player = player;
  }

  public int getPawnNumber() {
    return pawnNumber;
  }

  public int getAbsoluteBoardPosition() {
    return Board.boardOffset.get(team) + boardPosition;
  }

  public int getRelativeBoardPosition() {
    return boardPosition;
  }

  public void setRelativeBoardposition(int pos) {
    boardPosition = pos;
  }

  public void addRelativeBoardPosition(int amount) {
    setRelativeBoardposition(boardPosition + amount);
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serializedPawn = new LinkedHashMap<>();
    serializedPawn.put("location", boardPosition);
    serializedPawn.put("owner", player.getId());
    return serializedPawn;
  }

  @Override
  public void update(DocumentSnapshot document) {}

  public void update(int position) {
    this.boardPosition = position;
  }
}
