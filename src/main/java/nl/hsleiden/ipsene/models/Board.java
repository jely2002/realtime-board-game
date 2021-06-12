package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Board implements Model {

  public static final int STEPS_BETWEEN_TEAMS = 15;
  public static final int START_POSITION_INDEX = 37;
  public static final int POOL_PLUS_END_SIZE = 9;
  public static final int HIGHEST_BOARD_POSITION = 99;

  private static final HashMap<PlayerColour, Integer> poolStartPosition =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> startPositions =
      new HashMap<PlayerColour, Integer>();
  private static final HashMap<PlayerColour, Integer> endPositions =
      new HashMap<PlayerColour, Integer>();
  private static HashMap<PlayerColour, ArrayList<Pawn>> endPools = new HashMap<>();
  private static HashMap<PlayerColour, Integer> endPoolStartPosition = new HashMap<>();
  static long turnStartTime;
  private ArrayList<View> observers = new ArrayList<>();

  static {
    for (PlayerColour c : PlayerColour.values()) {
      endPools.put(c, new ArrayList<Pawn>());
    }
    poolStartPosition.put(PlayerColour.GREEN, 1);
    startPositions.put(PlayerColour.GREEN, 37);
    endPositions.put(PlayerColour.GREEN, 99);
    endPoolStartPosition.put(PlayerColour.GREEN, 4);

    poolStartPosition.put(PlayerColour.BLUE, 10);
    startPositions.put(PlayerColour.BLUE, 52);
    endPositions.put(PlayerColour.BLUE, 50);
    endPoolStartPosition.put(PlayerColour.BLUE, 13);

    poolStartPosition.put(PlayerColour.YELLOW, 19);
    startPositions.put(PlayerColour.YELLOW, 69);
    endPositions.put(PlayerColour.YELLOW, 67);
    endPoolStartPosition.put(PlayerColour.YELLOW, 22);

    poolStartPosition.put(PlayerColour.RED, 28);
    startPositions.put(PlayerColour.RED, 84);
    endPositions.put(PlayerColour.RED, 82);
    endPoolStartPosition.put(PlayerColour.GREEN, 31);
  }

  public Board() {}

  public static boolean isInEndPosition(PlayerColour colour, int pos) {
    return endPositions.get(colour) == pos;
  }

  public static void putPawnIntoEndPool(PlayerColour colour, Pawn pawn) {
    endPools.get(colour).add(pawn);
    pawn.setBoardPosition(endPoolStartPosition.get(colour) + endPools.get(colour).size());
    pawn.setIsInsideEndPool(true);
  }

  public static int getFirstPoolPosition(PlayerColour team) {
    return poolStartPosition.get(team);
  }

  public static int getFirstBoardPosition(PlayerColour team) {
    return startPositions.get(team);
  }

  public static boolean isInsidePool(PlayerColour team, int position) {
    return (position < getFirstBoardPosition(team));
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
