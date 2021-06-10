package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class BoardOLD implements Model {
  public static final int STEPS_BETWEEN_TEAMS = 14;
  public static final HashMap<TeamType, Integer> boardOffset = new HashMap<TeamType, Integer>();

  private List<View> observers = new ArrayList<>();

  static {
    boardOffset.put(TeamType.RED, 0);
    boardOffset.put(TeamType.GREEN, STEPS_BETWEEN_TEAMS);
    boardOffset.put(TeamType.BLUE, STEPS_BETWEEN_TEAMS * 2);
    boardOffset.put(TeamType.YELLOW, STEPS_BETWEEN_TEAMS * 3);
  }

  @Override
  public void registerObserver(View v) {
    observers.add(v);
  }

  @Override
  public void unregisterObserver(View v) {
    observers.remove(v);
  }

  @Override
  public void notifyObservers() {
    observers.forEach(View::update);
  }
}
