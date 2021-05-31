package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

import java.util.ArrayList;
import java.util.List;

public class Team implements Model {
  private Pawn[][] pawns;
  public static final int PAWNS_PER_PLAYER = 2; // idk
  public static final int PLAYERS_PER_TEAM = 2;

  /** @param teamtype the type of the team, given to the pawns created in the constructor */
  public Team(TeamType teamtype) {
    pawns = new Pawn[PLAYERS_PER_TEAM][PAWNS_PER_PLAYER];
    int pawnNum = 0;
    for (int i = 0; i < PAWNS_PER_PLAYER; i++) {
      for (int j = 0; j < PLAYERS_PER_TEAM; j++) {
        pawns[i][j] = new Pawn(teamtype, pawnNum);
        ++pawnNum;
      }
    }
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
