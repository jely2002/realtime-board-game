package nl.hsleiden.ipsene.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nl.hsleiden.ipsene.models.Board;


public class CountdownTimer implements Runnable{
    static final double nanoFactor = Math.pow(10, 9);
    static int maxTurnTime = 60;
    private final Board board;
    private Label label;

    public CountdownTimer(Board board, Label label) {
        this.board = board;
        this.label = label;
    }

    @Override
    public void run() {
        while (maxTurnTime > 0){
            Platform.runLater(() -> {
                    label.setText(String.valueOf(maxTurnTime));
                    });

            maxTurnTime--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
