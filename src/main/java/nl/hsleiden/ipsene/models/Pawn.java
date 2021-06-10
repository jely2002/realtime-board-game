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
  private boolean isInsidePool = true;
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
  public TeamType getTeamType() { return team; }

  public void setOwningPlayer(Player player) {
    this.player = player;
  }

  public int getPawnNumber() {
    return pawnNumber;
  }

  public int getBoardPosition() {
    if (isInsidePool) {
      System.out.println("got pool pos");
      return Board.getFirstPoolPosition(team) + boardPosition + 1;
    }
    System.out.println("got board pos");
    return Board.getFirstBoardPosition(team) + boardPosition + 1;
  }

  public int getRelativeRealBoardPosition() {
    return boardPosition;
  }

  public void setRelativeBoardposition(int pos) {
    // if pos has changed then pawn is now outside pool
    isInsidePool = (Board.isInsidePool(team, pos));
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
    this.setRelativeBoardposition(position);
  }
}
