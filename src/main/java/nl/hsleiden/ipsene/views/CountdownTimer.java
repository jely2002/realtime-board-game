package nl.hsleiden.ipsene.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nl.hsleiden.ipsene.application.Main;
import nl.hsleiden.ipsene.controllers.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownTimer implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  private GameController gameController;

  private int turnTime;
  private final Label label;
  private int labelX;
  private int labelY;

  private final int YOFFSET = 30;
  private final int XOFFSET = 40;
  /**
   * SLEEPTIME is in mili sec
   */
  private final int SLEEPTIME = 1000;
  /**
   * MAXTURNTIME and MINTURNTIME in sec
   */
  private final int SMALLESTTWODIGIT = 10;
  private final int ENDTIME = 0;

  public CountdownTimer(GameController gameController, Label label, int turnTime, int labelX, int labelY) {
    this.label = label;
    this.turnTime = turnTime;
    this.labelX = labelX;
    this.labelY = labelY;
    this.gameController = gameController;
  }

  @Override
  public void run() {
    String numberAsString;
    ViewHelper.setNodeCoordinates(label, labelX, labelY);
    label.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 120; -fx-text-fill: #000000");
    int countDownTime = turnTime;
    //System.out.println("Thread is running");
    while (!Thread.interrupted()) {
      System.out.println("Thread is running");
      if (countDownTime >= SMALLESTTWODIGIT) {
        numberAsString = Integer.toString(countDownTime);
      }
      else if (countDownTime < SMALLESTTWODIGIT && countDownTime > ENDTIME) {
        numberAsString = "0" + Integer.toString(countDownTime);
      } else {
        break;
      }
      String finalNumberAsString = numberAsString;
      Platform.runLater(() -> label.setText(String.valueOf(finalNumberAsString)));
      countDownTime--;
      try {
        Thread.sleep(SLEEPTIME);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
        break;
      }
    }
    if (countDownTime==0) {
      Platform.runLater(
        () -> {
          label.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 40; -fx-text-fill: #000000");
          ViewHelper.setNodeCoordinates(label, labelX - XOFFSET, labelY + YOFFSET);
          label.setText("Time's up!");
        });
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Platform.runLater(() -> {
        System.out.println("Thread is stopped");
        gameController.backToMainMenu();
      });
    }
  }
}
