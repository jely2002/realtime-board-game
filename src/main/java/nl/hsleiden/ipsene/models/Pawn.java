package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

public class Pawn implements Model {
  private int boardPosition;
  private final TeamType team;
  private Player player;
  private int pawnNumber;

  /**
   * @param team - the 'type' of the team, from enum TeamType used to get team info and calculate
   *     pos
   * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board
   *     position
   */
  public Pawn(TeamType team, int pawnNum) {
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
  public void registerObserver(View v) {}

  @Override
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {}
}
