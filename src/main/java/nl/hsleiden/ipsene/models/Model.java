package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

public interface Model {
  public void registerObserver(View v);

  public void unregisterObserver(View v);

  public void notifyObservers();
}
