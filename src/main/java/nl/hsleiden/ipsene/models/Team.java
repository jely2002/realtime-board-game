package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.hsleiden.ipsene.controllers.CardController;
import nl.hsleiden.ipsene.observers.TeamObservable;
import nl.hsleiden.ipsene.observers.TeamObserver;

public class Team implements TeamObservable {
  private ArrayList<HashMap<Integer, Pawn>> pawns;
  private Player[] players;
  public static final int PAWNS_PER_PLAYER = 2; // idk
  public static final int PLAYERS_PER_TEAM = 2;

  /** @param teamtype the type of the team, given to the pawns created in the constructor */
  public Team(TeamType teamtype) {
    pawns = new ArrayList<>();
    players = new Player[PLAYERS_PER_TEAM];
    int pawnNum = 0;
    // create pawns and player
    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
      pawns.add(new HashMap<>());
      for (int j = 0; j < PAWNS_PER_PLAYER; j++) {
        pawns.get(i).put(pawnNum, new Pawn(teamtype, pawnNum));
        ++pawnNum;
      }
      players[i] = new Player(this, i);
    }
  }

  /**
   * calls doTurn() on all players
   */
  public void doTurn() {
    for (int i = 0; i < players.length; i++) {
      players[i].doTurn();
    }
  }

  /** draws a number of cards per player
   * @param amountOfCardsPerPlayer the amount off cards drawn per player
   * @param controller the card controller drawn from
   */
  public void distributeCards(int amountOfCardsPerPlayer, CardController controller) {
    for (int i = 0; i < players.length; i++) {
      for (int j = 0; j < amountOfCardsPerPlayer; j++) {
        players[i].addCard(controller.drawCard());
      }
    }
  }

  /** gets a pawn with a given index from a given player
   * @param playerIndex index of the player within their team
   * @param pawnIndex index of the pawn to get
   * @return the corresponding pawn
   */
  public Pawn getPawn(int playerIndex, int pawnIndex) {
    if (playerIndex < PLAYERS_PER_TEAM)
      return pawns.get(playerIndex).get(pawnIndex);
    return null;
  }
  private List<TeamObserver> observers = new ArrayList<TeamObserver>();

  // Add an observer to the list
  public void register(TeamObserver to) {
    observers.add(to);
  }
  // Signal all observers that something has changed.
  // Also send <<this>> object to the observers.
  public void notifyAllObservers() {
    for (TeamObserver to : observers) {
      to.update((TeamObserver) this);
    }
  }
}
