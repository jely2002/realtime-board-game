package nl.hsleiden.ipsene.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;

import nl.hsleiden.ipsene.exceptions.PlayerIndexNotFoundException;
import nl.hsleiden.ipsene.firebase.Firebase;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game implements Model, FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Game.class.getName());

  private static final int AMOUNT_OF_TEAMS = 2;
  private static final int TOKEN_LENGTH = 5;

  private final ArrayList<View> observers = new ArrayList<>();
  private final ArrayList<Team> teams;

  private String token;
  private final Deck deck;

  private Integer ownPlayer;
  private int doingTurn;
  private int round;
  private Timestamp turnStartTime;
  private int cardsPerPlayerNextRound = 0;

  public Game() {
    this.token = generateToken(TOKEN_LENGTH);
    this.round = 0;
    this.teams = generateTeams();
    this.deck = new Deck(4, this);
    setCardsToBeDrawnNextTurn(5);
    distributeCards(deck);
  }

  private ArrayList<Team> generateTeams() {
    ArrayList<Team> teams = new ArrayList<>();
    TeamType[] types = {TeamType.RED, TeamType.GREEN, TeamType.BLUE, TeamType.YELLOW};
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

  public void doTurns() {
    for (Team team : this.teams) {
      team.doTurn();
    }
  }

  public void distributeCards(Deck deck) {
    for (Team team : this.teams) {
      team.distributeCards(cardsPerPlayerNextRound, deck);
    }
  }

  public void setCardsToBeDrawnNextTurn(int amount) {
    cardsPerPlayerNextRound = amount;
  }

  @Override
  public void update(DocumentSnapshot document) {

    doingTurn = Math.toIntExact(document.getLong("doingTurn"));
    round = Math.toIntExact(document.getLong("round"));
    turnStartTime = document.getTimestamp("turnStartTime");
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

  public Player getPlayer(int absolutePlayerId) throws PlayerIndexNotFoundException {
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

  public void setDoingTurn(int doingTurn) {
    this.doingTurn = doingTurn;
  }

  public int getRound() {
    return round;
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
