package nl.hsleiden.ipsene.interfaces;

public interface Model {
  void registerObserver(View v);

  void unregisterObserver(View v);

  void notifyObservers();
}
