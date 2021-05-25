package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.List;
import nl.hsleiden.ipsene.observers.LobbyObservable;
import nl.hsleiden.ipsene.observers.LobbyObserver;

public class Lobby implements LobbyObservable {
  private List<LobbyObserver> observers = new ArrayList<LobbyObserver>();

  // Add an observer to the list
  public void register(LobbyObserver lo) {
    observers.add(lo);
  }
  // Signal all observers that something has changed.
  // Also send <<this>> object to the observers.
  public void notifyAllObservers() {
    for (LobbyObserver lo : observers) {
      lo.update((LobbyObserver) this);
    }
  }
}
