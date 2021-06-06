package nl.hsleiden.ipsene.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import nl.hsleiden.ipsene.views.GameStatView;
import nl.hsleiden.ipsene.views.View;

public class GameStat implements Model {

  static long turnStartTime;
  static GameStatView gameStatView;
  private List<View> observers = new ArrayList<>();

  public GameStat() {
    gameStatView = GameStatView.getInstance();
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
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {
    for (View v : observers) {
      try {
        v.update();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

class CountDownTimer extends Thread {
  static final double nanoFactor = Math.pow(10, 9);
  static final long maxTurnTime = (long) (60 * nanoFactor);
  private final long turnStartTime;
  private final GameStat gameStat;

  CountDownTimer(long turnStartTime, GameStat gameStat) {
    this.turnStartTime = turnStartTime;
    this.gameStat = gameStat;
  }

  @Override
  public void run() {
    while ((System.nanoTime() - turnStartTime) < maxTurnTime) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Platform.runLater(gameStat::notifyObservers);
    }
  }
}
