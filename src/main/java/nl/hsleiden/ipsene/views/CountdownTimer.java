package nl.hsleiden.ipsene.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.controllers.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownTimer implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  private final GameController gameController;

  private final int turnTime;
  private final Label label;
  private final int labelX;
  private final int labelY;

  private final int YOFFSET = 30;
  private final int XOFFSET = 40;
  /** SLEEPTIME is in mili sec */
  private final int SLEEPTIME = 1000;
  /** MAXTURNTIME and MINTURNTIME in sec */
  private final int SMALLEST_TWO_DIGIT = 10;

  private final int ENDTIME = 0;

  public CountdownTimer(
      GameController gameController, Label label, int turnTime, int labelX, int labelY) {
    this.label = label;
    this.turnTime = turnTime;
    this.labelX = labelX;
    this.labelY = labelY;
    this.gameController = gameController;
  }

  @Override
  public void run() {
    /** numberAsString help variable for converting integer to string */
    String numberAsString;
    ViewHelper.setNodeCoordinates(label, labelX, labelY);
    label.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
    int countDownTime = turnTime;
    while (!Thread.interrupted()) {
      if (countDownTime >= SMALLEST_TWO_DIGIT) {
        numberAsString = Integer.toString(countDownTime);
      } else if (countDownTime < SMALLEST_TWO_DIGIT && countDownTime > ENDTIME) {
        numberAsString = "0" + countDownTime;
      } else {
        break;
      }
      String finalNumberAsString = numberAsString;
      Platform.runLater(() -> label.setText(finalNumberAsString));
      countDownTime--;
      try {
        Thread.sleep(SLEEPTIME);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
        break;
      }
    }
    if (countDownTime == 0) {
      Platform.runLater(
          () -> {
            label.setStyle(
                "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 40; -fx-text-fill: #000000");
            ViewHelper.setNodeCoordinates(label, labelX - XOFFSET, labelY + YOFFSET);
            label.setText("Time's up!");
          });
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
      }
      Platform.runLater(
          () -> {
            if(gameController.getIdCurrentPlayer() == gameController.getOwnPlayer().getId()) {
              gameController.surrender();
            }
          });
    }
  }
}
