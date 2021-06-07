package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import nl.hsleiden.ipsene.controllers.CardController;
import nl.hsleiden.ipsene.views.View;

public class Team implements Model {

  private Player[] players;
  public static final int PAWNS_PER_PLAYER = 2; // idk
  public static final int PLAYERS_PER_TEAM = 2;

  /** @param teamtype the type of the team, given to the pawns created in the constructor */
  public Team(TeamType teamtype) {
    int pawnNumber = 0;
    players = new Player[PLAYERS_PER_TEAM];

    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
      ArrayList<Pawn> pawns = new ArrayList<>(PAWNS_PER_PLAYER);

      for (int j = 0; j < PAWNS_PER_PLAYER; j++) {
        pawns.add(new Pawn(teamtype, pawnNumber));
        ++pawnNumber;
      }

      players[i] = new Player(this, i, pawns);
    }
  }

  /** calls doTurn() on all players */
  public void doTurn() {
    for (int i = 0; i < players.length; i++) {
      players[i].doTurn();
    }
  }

  /**
   * draws a number of cards per player
   *
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

  /**
   * gets a pawn with a given index from a given player
   *
   * @param playerIndex index of the player within their team
   * @param pawnIndex index of the pawn to get
   * @return the corresponding pawn
   */
  public Pawn getPawn(int playerIndex, int pawnIndex) {
    if (playerIndex < PLAYERS_PER_TEAM) return players[playerIndex].getPawn(pawnIndex);
    return null;
  }

  @Override
  public void registerObserver(View v) {}

  @Override
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {}
}
