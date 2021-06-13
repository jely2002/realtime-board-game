package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Board implements Model {

  public static final int START_POSITION_INDEX = 37;
  public static final int HIGHEST_BOARD_POSITION = 101;
  public static final int END_POOL_SIZE = 4;

  private static final HashMap<PlayerColour, Integer> poolStartPosition =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> startPositions =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> endPositions =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, ArrayList<Pawn>> endPools = new HashMap<>();
  private static final HashMap<PlayerColour, Integer> endPoolStartPosition = new HashMap<>();
  private final ArrayList<View> observers = new ArrayList<>();

  static {
    for (PlayerColour c : PlayerColour.values()) {
      endPools.put(c, new ArrayList<Pawn>());
    }
    poolStartPosition.put(PlayerColour.GREEN, 1);
    startPositions.put(PlayerColour.GREEN, 37);
    endPositions.put(PlayerColour.GREEN, 100);
    endPoolStartPosition.put(PlayerColour.GREEN, 5);

    poolStartPosition.put(PlayerColour.BLUE, 10);
    startPositions.put(PlayerColour.BLUE, 52);
    endPositions.put(PlayerColour.BLUE, 51);
    endPoolStartPosition.put(PlayerColour.BLUE, 14);

    poolStartPosition.put(PlayerColour.YELLOW, 19);
    startPositions.put(PlayerColour.YELLOW, 69);
    endPositions.put(PlayerColour.YELLOW, 68);
    endPoolStartPosition.put(PlayerColour.YELLOW, 23);

    poolStartPosition.put(PlayerColour.RED, 28);
    startPositions.put(PlayerColour.RED, 84);
    endPositions.put(PlayerColour.RED, 83);
    endPoolStartPosition.put(PlayerColour.RED, 32);
  }

  private Board() {}

  private static Board board;

  public static Board getInstance() {
    if (board == null) {
      return new Board();
    }
    return board;
  }

  public boolean isInEndPosition(PlayerColour colour, int pos) {
    return endPositions.get(colour) == pos;
  }

  public void putPawnIntoEndPool(PlayerColour colour, Pawn pawn) {
    endPools.get(colour).add(pawn);
    pawn.setBoardPosition(endPoolStartPosition.get(colour) + endPools.get(colour).size());
    pawn.setIsInsideEndPool(true);
    notifyObservers();
  }

  public void emptyEndPools() {
    for (PlayerColour colour : endPools.keySet()) {
      endPools.get(colour).clear();
    }
  }

  /** @return null if the game has not been won, else the colour that won the game */
  public PlayerColour hasTheGameBeenWon() {
    for (PlayerColour colour : endPools.keySet()) {
      ArrayList<Pawn> pool = endPools.get(colour);
      if (pool.size() >= END_POOL_SIZE) {
        return colour;
      }
    }
    return null;
  }

  public static int getFirstPoolPosition(PlayerColour team) {
    return poolStartPosition.get(team);
  }

  /**
   * @param colour the team
   * @return gets the start position on the board for the team
   */
  public static int getFirstBoardPosition(PlayerColour colour) {
    return startPositions.get(colour);
  }

  /**
   * @param team the pawns team
   * @param position the pawns position
   * @return wheter this position is inside the teams end pool
   */
  public static boolean isInsideEndPool(PlayerColour team, int position) {
    int startpos = endPoolStartPosition.get(team);
    return (position > startpos && position <= END_POOL_SIZE + startpos);
  }

  @Override
  public void registerObserver(View v) {
    observers.add(v);
  }

  @Override
  public void unregisterObserver(View v) {
    observers.remove(v);
  }

  @Override
  public void notifyObservers() {
    observers.forEach(View::update);
  }
}
