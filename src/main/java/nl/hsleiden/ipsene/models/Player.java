package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

import java.util.ArrayList;
import java.util.List;

public class Player implements Model {
  private ArrayList<Card> cards;

  public Player() {
    cards = new ArrayList<Card>();
  }

  public void addCard() {
    // push back a card and check bounds
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
