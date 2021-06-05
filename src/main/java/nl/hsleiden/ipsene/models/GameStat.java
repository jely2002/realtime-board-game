package nl.hsleiden.ipsene.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import nl.hsleiden.ipsene.views.GameStatView;
import nl.hsleiden.ipsene.views.View;

public class GameStat {

  static long turnStartTime;
  static GameStatView gameStatView;
  // List of all Observers of this Observable Objects
  private List<GameStatView> observers = new ArrayList<GameStatView>();

  public GameStat() {
    gameStatView = GameStatView.getInstance();
  }

  public int getCurrentTurnTime() {
    double nanoFactor = Math.pow(10, 9);
    long remainingNano = (long) (60 * nanoFactor - (System.nanoTime() - turnStartTime));
    int remainingSecs = (int) (remainingNano / nanoFactor);
    return remainingSecs;
  }

  public void startTurnTimer() {
    turnStartTime = System.nanoTime();
    CountDownTimer countDownTimer = new CountDownTimer(turnStartTime, this);
    countDownTimer.start();
  }

  public void registerObserver(GameStatView v) {
    observers.add(v);
  }

  public void unregisterObserver(View v) {}

  public void notifyObservers() {
    for (GameStatView v : observers) {
      try {
        v.update(this);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

class CountDownTimer extends Thread {
  static final double nanoFactor = Math.pow(10, 9);
  static final long maxTurnTime = (long) (60 * nanoFactor);
  private long turnStartTime;
  private GameStat gameStat;

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
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              gameStat.notifyObservers();
            }
          });
    }
  }
}
