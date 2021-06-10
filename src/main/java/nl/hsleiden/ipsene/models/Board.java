package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.views.BoardView;

public class Board implements Model {

  public static final int STEPS_BETWEEN_TEAMS = 15;
  public static final int START_POSITION_INDEX = 35;
  public static final int POOL_PLUS_END_SIZE = 9;

  private static final HashMap<TeamType, Integer> boardOffset = new HashMap<TeamType, Integer>();

  static long turnStartTime;
  private ArrayList<View> observers = new ArrayList<>();

  static {
    boardOffset.put(TeamType.GREEN, 0);
    boardOffset.put(TeamType.BLUE, 1);
    boardOffset.put(TeamType.YELLOW, 2);
    boardOffset.put(TeamType.RED, 3);
  }

  public Board() {

  }
  public static int getFirstPoolPosition(TeamType team) {
    return POOL_PLUS_END_SIZE * boardOffset.get(team);
  }
  public static int getFirstBoardPosition(TeamType team) {
    return START_POSITION_INDEX + (STEPS_BETWEEN_TEAMS * boardOffset.get(team));
  }
  public static boolean isInsidePool(TeamType team, int position) {
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
