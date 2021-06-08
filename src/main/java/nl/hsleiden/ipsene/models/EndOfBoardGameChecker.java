package nl.hsleiden.ipsene.models;

import javafx.application.Platform;

public class EndOfBoardGameChecker extends Thread {
    private Board board;


    EndOfBoardGameChecker(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        while ((board.nPlayerCount == 4) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            board.quit();
        });
    }
}
