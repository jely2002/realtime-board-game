package nl.hsleiden.ipsene.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import nl.hsleiden.ipsene.controllers.LobbyController;
import nl.hsleiden.ipsene.firebase.Firebase;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;

public class Game implements Model, FirebaseSerializable<Map<String, Object>> {

  public static final int AMOUNT_OF_TEAMS = 2;
  public static final int AMOUNT_OF_PLAYERS = 4;
  public static final int AMOUNT_OF_SMALL_ROUNDS = 3;
  private static final int TOKEN_LENGTH = 5;

  private final ArrayList<View> observers = new ArrayList<>();
  private final ArrayList<Team> teams;

  private String token;
  private final Deck deck;

  private Integer ownPlayer;
  private int doingTurn;
  private int playerToGoFirst;
  private int round;
  private int bigRound;
  private int smallRound;

  private Timestamp turnStartTime;
  private int cardsPerPlayerNextRound = 5;
  private int cardsThisTurnValue = 2;
  private boolean gameHasStarted = false;

  private final LobbyController lobbyController;

  public Game(LobbyController lobbyController) {
    this.token = generateToken(TOKEN_LENGTH);
    this.round = 0;
    this.teams = generateTeams();
    this.deck = new Deck(4, this);
    this.lobbyController = lobbyController;
    this.doingTurn = 0;
    distributeCards();
  }

  public boolean hasGameStarted() {
    return gameHasStarted;
  }

  public void setGameHasStarted(boolean started) {
    gameHasStarted = started;
  }

  public int getRound() {
    return round;
  }

  public int getBigRound() {
    return bigRound;
  }

  public int getSmallRound() {
    return smallRound;
  }

  /**
   * Called when the last smallRound has been completed. Changes the player who gets to play the
   * smallRounds first, shuffles the deck and starts a new smallRound.
   */
  public void advanceBigRound() {
    bigRound++;
    indexToNextPlayerToGoFirst();
    indexToNextPlayer();
    deck.regenerate(); // Shuffle the deck
    startNewSmallRound();
  }

  /**
   * Called after starting a new bigRound, or after completing a smallRound except for the last one.
   */
  public void startNewSmallRound() {
    doingTurn = playerToGoFirst;
    distributeCards();
  }

  /**
   * Called if there are no players left who have cards in their hand. If we are in the last
   * smallRound we go to the next bigRound. Otherwise, we start a new round.
   */
  public void EndOfSmallRound() {
    if (smallRound == AMOUNT_OF_SMALL_ROUNDS) {
      smallRound = 0;
      advanceBigRound();
    } else {
      startNewSmallRound();
    }
  }

  public void indexToNextPlayer() {
    if (doingTurn == AMOUNT_OF_PLAYERS - 1) {
      doingTurn = 0;
    } else {
      doingTurn++;
    }
  }

  public void indexToNextPlayerToGoFirst() {
    if (playerToGoFirst == AMOUNT_OF_PLAYERS - 1) {
      playerToGoFirst = 0;
    } else {
      playerToGoFirst++;
    }
  }

  /**
   * @return true if all players have no cards in their hand, false if there is at least one player
   *     with cards left.
   */
  public boolean noPlayerHasCardsLeft() {
    for (Player player : getAllPlayers()) {
      if (!player.getCards().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private ArrayList<Team> generateTeams() {
    ArrayList<Team> teams = new ArrayList<>();
    PlayerColour[][] types = {
      {PlayerColour.RED, PlayerColour.GREEN}, {PlayerColour.BLUE, PlayerColour.YELLOW}
    };
    for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
      teams.add(new Team(types[i], i));
    }
    return teams;
  }

  private String generateToken(int length) {
    StringBuilder sb = new StringBuilder(length);
    Random random = new Random();
    for (int i = 0; i < length; i++) sb.append((char) ('0' + random.nextInt(10)));
    return sb.toString();
  }

  /** Distribute cards to all teams */
  private void distributeCards() {
    for (Team team : this.teams) {
      team.distributeCards(cardsPerPlayerNextRound, deck);
    }
  }

  @Override
  public void update(DocumentSnapshot document) {

    doingTurn = Math.toIntExact(document.getLong(Firebase.DOING_TURN_FIELD_NAME));
    round = Math.toIntExact(document.getLong(Firebase.ROUND_FIELD_NAME));
    bigRound = Math.toIntExact(document.getLong(Firebase.BIG_ROUND_FIELD_NAME));
    smallRound = Math.toIntExact(document.getLong(Firebase.SMALL_ROUND_FIELD_NAME));
    turnStartTime = document.getTimestamp(Firebase.TURN_START_TIME_FIELD_NAME);
    gameHasStarted = document.getBoolean(Firebase.GAME_HAS_STARTED_START_FIELD_NAME);
    token = document.getId();

    // empty the pools so the pawns can add themselves again
    Board.getInstance().emptyEndPools();

    teams.forEach(team -> team.update(document));
    deck.update(document);

    notifyObservers();
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serializedGame = new LinkedHashMap<>();

    LinkedHashMap<String, Object> serializedPlayers = new LinkedHashMap<>();
    for (Team team : teams) {
      serializedPlayers.putAll(team.serialize());
    }

    serializedGame.put(Firebase.PLAYER_FIELD_NAME, serializedPlayers);
    serializedGame.put(Firebase.CARD_FIELD_NAME, deck.serialize());
    serializedGame.put(Firebase.ROUND_FIELD_NAME, round);
    serializedGame.put(Firebase.BIG_ROUND_FIELD_NAME, bigRound);
    serializedGame.put(Firebase.SMALL_ROUND_FIELD_NAME, smallRound);
    serializedGame.put(Firebase.TURN_START_TIME_FIELD_NAME, turnStartTime);
    serializedGame.put(Firebase.DOING_TURN_FIELD_NAME, doingTurn);
    serializedGame.put(Firebase.GAME_HAS_STARTED_START_FIELD_NAME, gameHasStarted);

    return serializedGame;
  }

  public Integer getOwnPlayer() {
    return ownPlayer;
  }

  public void setOwnPlayer(Integer ownPlayer) {
    this.ownPlayer = ownPlayer;
  }

  public ArrayList<Team> getTeams() {
    return teams;
  }

  /**
   * @param absolutePlayerId the players id
   * @return player with a given id
   */
  public Player getPlayer(int absolutePlayerId) {
    int playerIndex = absolutePlayerId;
    int teamIndex = 0;
    if (absolutePlayerId >= 2) {
      playerIndex =
          (int)
              (Math.round((double) absolutePlayerId / (double) AMOUNT_OF_TEAMS)
                  - (AMOUNT_OF_TEAMS - 1));
      teamIndex = absolutePlayerId - playerIndex - 1;
    }
    Team team = teams.get(teamIndex);
    return team.getPlayer(playerIndex);
  }

  public String getToken() {
    return token;
  }

  public int getDoingTurn() {
    return doingTurn;
  }

  public void setDoingTurnPlayer(int doingTurn) {
    this.doingTurn = doingTurn;
  }

  public Timestamp getTurnStartTime() {
    return turnStartTime;
  }

  public void backToMainMenu() {
    this.lobbyController.backToMainMenu();
  }

  /**
   * Get all the Players objects in the game. We currently have 4 of them.
   *
   * @return ArrayList with Player objects.
   */
  public ArrayList<Player> getAllPlayers() {
    ArrayList<Team> teams = getTeams();
    ArrayList<Player> players = new ArrayList<>();
    for (Team team : teams) {
      players.addAll(Arrays.asList(team.getPlayers()));
    }
    return players;
  }

  /**
   * Loops through all the players and counts how many of them have cards.
   *
   * @return the amount of players that have one or more cards.
   */
  public int amountOfPlayersWithCards() {
    int count = 0;
    for (Player player : getAllPlayers()) {
      if (!player.getCards().isEmpty()) count++;
    }
    return count;
  }

  @Override
  public void registerObserver(View v) {
    this.observers.add(v);
  }

  @Override
  public void unregisterObserver(View v) {
    this.observers.remove(v);
  }

  @Override
  public void notifyObservers() {
    observers.forEach(View::update);
  }
}
