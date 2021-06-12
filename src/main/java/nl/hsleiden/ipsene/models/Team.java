package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Team implements FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Team.class.getName());

  private final Game game;

  private final Player[] players;
  public static final int PAWNS_PER_PLAYER = 4;
  public static final int PLAYERS_PER_TEAM = 2;
  public final int teamIndex;

  /** @param playerColours the type of the team, given to the pawns created in the constructor */
  public Team(PlayerColour[] playerColours, int teamIndex, Game game) {
    this.game = game;
    this.teamIndex = teamIndex;

    players = new Player[PLAYERS_PER_TEAM];
    int position = 0;
    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
      ArrayList<Pawn> pawns = new ArrayList<>(PAWNS_PER_PLAYER);
      int pawnNumber = 0;

      for (int j = 0; j < PAWNS_PER_PLAYER; j++) {
        pawns.add(new Pawn(playerColours[i], pawnNumber, game));
        ++pawnNumber;
      }
      int absolutePlayerId = i;
      if (teamIndex > 0) {
        absolutePlayerId = i + (teamIndex + 1);
      }
      players[i] = new Player(this, playerColours[i], absolutePlayerId, i, pawns, game);
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
  public Pawn getPawn(int playerIndex, int pawnIndex) {
    if (playerIndex < PLAYERS_PER_TEAM) return players[playerIndex].getPawn(pawnIndex);
    return null; // do NOT remove NULL is used by Player
  }

  public Player[] getPlayers() {
    return players;
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
