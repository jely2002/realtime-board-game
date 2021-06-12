package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.cloud.Tuple;
import javafx.application.Platform;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Board implements Model {

  public static final int STEPS_BETWEEN_TEAMS = 15;
  public static final int START_POSITION_INDEX = 37;
  public static final int POOL_PLUS_END_SIZE = 9;
  public static final int HIGHEST_BOARD_POSITION = 101;
  public static final int END_POOL_SIZE = 4;

  private static final HashMap<PlayerColour, Integer> poolStartPosition =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> startPositions =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> endPositions =
      new HashMap<PlayerColour, Integer>();
  private static HashMap<PlayerColour, ArrayList<Pawn>> endPools = new HashMap<>();
  private static final HashMap<PlayerColour, Integer> endPoolStartPosition = new HashMap<>();
  private static long turnStartTime;
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

  private Board() {
  }

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
    System.out.println("created new pools");
    for (PlayerColour colour : endPools.keySet()) {
      endPools.get(colour).clear();
    }
  }

  /**
   * @return null if the game has not been won, else the colour that won the game
   */
  public PlayerColour hasTheGameBeenWon() {
    for (PlayerColour colour : endPools.keySet()) {
      ArrayList<Pawn> pool = endPools.get(colour);
      System.out.println("colour: " + colour.toString() + " size: " + pool.size());
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
   * @param colour the colour
   * @param position the position
   * @return wheter the position is inside the starting pool of the given colour
   */
  public static boolean isInsidePool(PlayerColour colour, int position) {
    return (position < getFirstBoardPosition(colour));
  }

  /**
   * @param team the pawns team
   * @param position the pawns position
   * @return wheter this position is inside the teams end pool
   */
  public boolean isInsideEndPool(PlayerColour team, int position) {
    int startpos = endPoolStartPosition.get(team);
    return (position > startpos && position <= END_POOL_SIZE + startpos);
  }

  /**
   * Returns the remaining amount of seconds of a turn
   *
   * @return remaining amount of seconds
   */
  public int getCurrentTurnTime() {
    double nanoFactor = Math.pow(10, 9);
    long remainingNano = (long) (60 * nanoFactor - (System.nanoTime() - turnStartTime));
    return (int) (remainingNano / nanoFactor);
  }

  /** Starts the turnTimer */
  public void startTurnTimer() {
    turnStartTime = System.nanoTime();
    CountDownTimer countDownTimer = new CountDownTimer(turnStartTime, this);
    countDownTimer.start();
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

class CountDownTimer extends Thread {
  static final double nanoFactor = Math.pow(10, 9);
  static final long maxTurnTime = (long) (60 * nanoFactor);
  private final long turnStartTime;
  private final Board board;

  CountDownTimer(long turnStartTime, Board board) {
    this.turnStartTime = turnStartTime;
    this.board = board;
  }

  @Override
  public void run() {
    while ((System.nanoTime() - turnStartTime) < maxTurnTime) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Platform.runLater(board::notifyObservers);
    }
  }
}
