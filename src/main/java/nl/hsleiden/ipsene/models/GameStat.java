package nl.hsleiden.ipsene.models;

import javafx.application.Platform;
import nl.hsleiden.ipsene.views.GameStatView;
import nl.hsleiden.ipsene.views.View;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class GameStat implements Model {

    static long turnStartTime;
    static GameStatView gameStatView;
    // List of all Observers of this Observable Objects
    private List<View> observers = new ArrayList<View>();

    public GameStat(){
        gameStatView = GameStatView.getInstance();
    }

    public int getCurrentTurnTime() {
        double nanoFactor = Math.pow(10, 9);
        long remainingNano = (long) (60*nanoFactor - (System.nanoTime() - turnStartTime));
        int remainingSecs = (int) (remainingNano/nanoFactor);
        return remainingSecs;
    }

    public void startTurnTimer() {
        turnStartTime = System.nanoTime();
        CountDownTimer countDownTimer = new CountDownTimer(turnStartTime, this);
        countDownTimer.start();
    }

    @Override
    public void registerObserver(View v) {
        observers.add(v);
    }

    public void unregisterObserver(View v) {

    }

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
    final static double nanoFactor = Math.pow(10, 9);
    final static long maxTurnTime = (long) (60 * nanoFactor);
    private long turnStartTime;
    private GameStat gameStat;
    CountDownTimer(long turnStartTime, GameStat gameStat){
        this.turnStartTime = turnStartTime;
        this.gameStat = gameStat;
    }

    @Override
    public void run() {
        while((System.nanoTime()-turnStartTime) < maxTurnTime){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameStat.notifyObservers();
                }
            });
        }
    }
}
