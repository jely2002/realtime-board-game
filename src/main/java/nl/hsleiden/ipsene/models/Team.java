package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.hsleiden.ipsene.exceptions.PlayerNotFoundException;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Team implements FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Team.class.getName());

  private final Game game;

  private final Player[] players;
  public static final int PAWNS_PER_PLAYER = 2; // idk
  public static final int PLAYERS_PER_TEAM = 2;
  public final int teamIndex;

  /** @param teamtype the type of the team, given to the pawns created in the constructor */
  public Team(TeamType teamtype, int teamIndex, Game game) {
    this.game = game;
    this.teamIndex = teamIndex;
    int pawnNumber = 0;
    players = new Player[PLAYERS_PER_TEAM];

    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
      ArrayList<Pawn> pawns = new ArrayList<>(PAWNS_PER_PLAYER);

      for (int j = 0; j < PAWNS_PER_PLAYER; j++) {
        pawns.add(new Pawn(teamtype, pawnNumber, game));
        ++pawnNumber;
      }
      int absolutePlayerId = i;
      if(teamIndex > 0) {
        absolutePlayerId = i + (teamIndex + 1);
      }
      players[i] = new Player(this, absolutePlayerId, i, pawns, game);
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
   * @param deck the deck to be drawn from
   */
  public void distributeCards(int amountOfCardsPerPlayer, Deck deck) {
    for (int i = 0; i < players.length; i++) {
      for (int j = 0; j < amountOfCardsPerPlayer; j++) {
        players[i].addCard(deck.drawCard());
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
    return null; // TODO Remove null return
  }

  public Player getPlayer(int playerIndex) {
    if (playerIndex < PLAYERS_PER_TEAM) return players[playerIndex];
    return null;
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serialized = new LinkedHashMap<>();
    for (Player player : players) {
      serialized.put(String.valueOf(player.getId()), player.serialize());
    }
    return serialized;
  }

  @Override
  public void update(DocumentSnapshot document) {
    Arrays.stream(players).forEach(player -> player.update(document));
  }
}
