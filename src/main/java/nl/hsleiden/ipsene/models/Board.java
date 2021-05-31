package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board implements Model {
  public static final int STEPS_BETWEEN_TEAMS = 14;
  public static final HashMap<TeamType, Integer> boardOffset = new HashMap<TeamType, Integer>();

  static {
    boardOffset.put(TeamType.RED, 0);
    boardOffset.put(TeamType.GREEN, STEPS_BETWEEN_TEAMS);
    boardOffset.put(TeamType.BLUE, STEPS_BETWEEN_TEAMS * 2);
    boardOffset.put(TeamType.YELLOW, STEPS_BETWEEN_TEAMS * 3);
  }

  @Override
  public void registerObserver(View v) {

  }

  @Override
  public void unregisterObserver(View v) {

  }

  @Override
  public void notifyObservers() {

  }
}
