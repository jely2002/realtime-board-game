package nl.hsleiden.ipsene.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.*;

import nl.hsleiden.ipsene.controllers.LobbyController;
import nl.hsleiden.ipsene.firebase.Firebase;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game implements Model, FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Game.class.getName());

  public static final int AMOUNT_OF_TEAMS = 2;
  private static final int TOKEN_LENGTH = 5;

  private final ArrayList<View> observers = new ArrayList<>();
  private final ArrayList<Team> teams;

  private String token;
  private final Deck deck;

  private Integer ownPlayer;
  private int doingTurn;
  private int round;
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

  public boolean hasGameStarted() { return gameHasStarted; }
  public void setGameHasStarted(boolean started) { gameHasStarted = started; }

  public int getRound() {
    return round;
  }

  /** */
  public void advanceRound() {
    round += 1;
    cardsPerPlayerNextRound = (cardsThisTurnValue == 1) ? 5 : 4;

    for (Team team : teams) {
      team.emptyCards();
    }
    distributeCards();
    if (cardsThisTurnValue < 3) {
      cardsThisTurnValue = cardsThisTurnValue + 1;
    } else {
      cardsThisTurnValue = 1;
      deck.regenerate();
    }
  }

  private ArrayList<Team> generateTeams() {
    ArrayList<Team> teams = new ArrayList<>();
    PlayerColour[][] types = {
      {PlayerColour.RED, PlayerColour.GREEN}, {PlayerColour.BLUE, PlayerColour.YELLOW}
    };
    for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
      teams.add(new Team(types[i], i, this));
    }
    return teams;
  }

  private String generateToken(int length) {
    StringBuilder sb = new StringBuilder(length);
    Random random = new Random();
    for (int i = 0; i < length; i++) sb.append((char) ('0' + random.nextInt(10)));
    return sb.toString();
  }

  /** distributes cards to all teams */
  private void distributeCards() {
    for (Team team : this.teams) {
      team.distributeCards(cardsPerPlayerNextRound, deck);
    }
  }

  @Override
  public void update(DocumentSnapshot document) {

    doingTurn = Math.toIntExact(document.getLong(Firebase.DOING_TURN_FIELD_NAME));
    round = Math.toIntExact(document.getLong(Firebase.ROUND_FIELD_NAME));
    turnStartTime = document.getTimestamp(Firebase.TURN_START_TIME_FIELD_NAME);
    gameHasStarted = document.getBoolean(Firebase.GAME_HAS_STARTED_START_FIELD_NAME);
    token = document.getId();

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

  public void setRound(int round) {
    this.round = round;
  }

  public Timestamp getTurnStartTime() {
    return turnStartTime;
  }

  public void setTurnStartTime(Timestamp turnStartTime) {
    this.turnStartTime = turnStartTime;
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
    ArrayList<Player> players = new ArrayList<Player>();
    for (Team team : teams) {
      players.addAll(Arrays.asList(team.getPlayers()));
    }
    Player player = teams.get(0).getPlayer(0);
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
