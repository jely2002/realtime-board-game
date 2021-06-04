package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

public class Pawn implements Model {
  private int boardPos;
  private final TeamType team;
  private final int pawnNum;

  /**
   * @param team - the 'type' of the team, from enum TeamType used to get team info and calculate
   *     pos
   * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board
   *     position
   */
  public Pawn(TeamType team, int pawnNum) {
    this.team = team;
    this.pawnNum = pawnNum;
    boardPos = pawnNum;
  }

  public int getPawnNumber() {
    return pawnNum;
  }

  public int getAbsoluteBoardPosition() {
    return Board.boardOffset.get(team) + boardPos;
  }

  public int getRelativeBoardPosition() {
    return boardPos;
  }

  public void setRelativeBoardposition(int pos) {
    boardPos = pos;
  }

  public void addRelativeBoardPosition(int amount) {
    setRelativeBoardposition(boardPos + amount);
  }

  @Override
  public void registerObserver(View v) {}

  @Override
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {}
}
