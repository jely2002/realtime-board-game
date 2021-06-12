package nl.hsleiden.ipsene.views;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class WaitingForPlayersThread implements Runnable {
  private final Label label;
  private final int DELAY = 750;
  private final String PREFIX = "Waiting on players";

  public WaitingForPlayersThread(Label label) {
    this.label = label;
  }

  private void doAndWait(String suffix) throws InterruptedException {
    Platform.runLater(
        () ->
            label.setText(
                PREFIX + suffix));
    Thread.sleep(DELAY);
  }

  @Override
  public void run() {
    try {
      while (true) {
        doAndWait("");
        doAndWait(".");
        doAndWait("..");
        doAndWait("...");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
