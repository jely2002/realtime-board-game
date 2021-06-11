package nl.hsleiden.ipsene.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nl.hsleiden.ipsene.application.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownTimer implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  private int turnTime;
  private final Label label;
  private final int labelX;
  private final int labelY;

  private final int yOffset = 30;
  private final int xOffset = 40;

  public CountdownTimer(Label label, int turnTime, int labelX, int labelY) {
    this.label = label;
    this.turnTime = turnTime;
    this.labelX = labelX;
    this.labelY = labelY;
  }

  @Override
  public void run() {
    ViewHelper.setNodeCoordinates(label, labelX, labelY);
    label.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
    while (turnTime > 10) {
      Platform.runLater(() -> label.setText(String.valueOf(turnTime)));
      turnTime--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
        break;
      }
    }
    while (turnTime <= 10 && turnTime > 0) {
      Platform.runLater(() -> label.setText("0" + turnTime));

      turnTime--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
        break;
      }
    }
    Platform.runLater(
        () -> {
          label.setStyle(
              "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 40; -fx-text-fill: #000000");
          ViewHelper.setNodeCoordinates(label, labelX - xOffset, labelY + yOffset);
          label.setText("Time's up!");
        });
  }
}
