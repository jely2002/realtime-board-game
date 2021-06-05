package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.GameStat;
import nl.hsleiden.ipsene.views.View;

public class GameStatController implements Controller {

  static GameStatController gameStatController = null;
  GameStat gameStat;

  private GameStatController() {
    gameStat = new GameStat();
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {
    gameStat.registerObserver(v);
  }

  public static GameStatController getInstance() {
    if (gameStatController == null) {
      gameStatController = new GameStatController();
    }
    return gameStatController;
  }

  /**
   * Returns the remaining amount of seconds of a turn from the model
   * @return remaining amount of seconds
   */
  public int getCurrentTime() {
    return gameStat.getCurrentTurnTime();
  }

  /**
   * Starts the turnTimer by calling the model
   */
  public void startTurnTimer() {
    gameStat.startTurnTimer();
  }
}
