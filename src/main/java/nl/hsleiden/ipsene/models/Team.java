package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import nl.hsleiden.ipsene.exceptions.OverdrawException;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.enums.PlayerColour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Team implements Model, FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Team.class.getName());

  private final Player[] players;
  public static final int PAWNS_PER_PLAYER = 4;
  public static final int PLAYERS_PER_TEAM = 2;
  public final int teamIndex;

  /** @param playerColours the type of the team, given to the pawns created in the constructor */
  public Team(PlayerColour[] playerColours, int teamIndex) {
    this.teamIndex = teamIndex;

    players = new Player[PLAYERS_PER_TEAM];
    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
      ArrayList<Pawn> pawns = new ArrayList<>(PAWNS_PER_PLAYER);
      int pawnNumber = 0;

      for (int j = 0; j < PAWNS_PER_PLAYER; j++) {
        pawns.add(new Pawn(playerColours[i], pawnNumber));
        ++pawnNumber;
      }
      int absolutePlayerId = i;
      if (teamIndex > 0) {
        absolutePlayerId = i + (teamIndex + 1);
      }
      players[i] = new Player(this, absolutePlayerId, i, pawns);
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
        try {
          players[i].addCard(deck.drawCard());
        } catch (OverdrawException e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
  }

  public void emptyCards() {
    for (int i = 0; i < players.length; i++) {
      players[i].emptyCards();
    }
  }

  /**
   * gets a pawn with a given index from a given player
   *
   * @param playerIndex index of the player within their team
   * @param pawnIndex index of the pawn to get
   * @return the corresponding pawn
   */
  public final Pawn getPawn(int playerIndex, int pawnIndex) {
    if (playerIndex < PLAYERS_PER_TEAM) return players[playerIndex].getPawn(pawnIndex);
    return null; // must return null to indicate the second pawn has not been selected
  }

  public final Player[] getPlayers() {
    return players;
  }

  public final Player getPlayer(int playerIndex) {
    if (playerIndex < PLAYERS_PER_TEAM) return players[playerIndex];
    return null;
  }

  public final Pawn[] getPawnsFromPlayer(int playerIndex) {
    return players[playerIndex].getPawns().toArray(new Pawn[0]);
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
