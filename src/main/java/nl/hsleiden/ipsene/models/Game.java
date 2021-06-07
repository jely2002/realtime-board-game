package nl.hsleiden.ipsene.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Game implements Model {

    private static final Logger logger = LoggerFactory.getLogger(Game.class.getName());

    private final UUID id;
    private final String token;

    private int doingTurn;
    private int round;
    private Timestamp turnStartTime;

    public static final int AMOUNT_OF_TEAMS = 2;
    private int cardsPerPlayerNextRound = 0;

    private final Deck deck;
    private final ArrayList<Team> teams;

    // When game is updated
    public Game(UUID id, String token, ArrayList<Team> teams, ArrayList<Card> cards) {
        this.id = id;
        this.token = token;
        this.teams = teams;
        this.deck = new Deck(cards);
    }

    // When new game is created
    public Game() {
        this.id = UUID.randomUUID();
        this.token = generateToken();
        this.round = 0;
        this.teams = generateTeams();
        this.deck = new Deck(4);
    }

    private ArrayList<Team> generateTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        TeamType[] types = {TeamType.RED, TeamType.GREEN, TeamType.BLUE, TeamType.YELLOW};
        for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
            teams.add(new Team(types[i], i));
        }
        return teams;
    }

    private String generateToken() {
        byte[] array = new byte[5];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
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

    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> serializedGame = new LinkedHashMap<>();

        LinkedHashMap<String, Object> serializedPlayers = new LinkedHashMap<>();
        for(Team team : teams) {
            serializedPlayers.putAll(team.serialize());
        }

        serializedGame.put("players", serializedPlayers);
        serializedGame.put("cards", deck.serialize());
        serializedGame.put("round", round);
        serializedGame.put("turnStartTime", turnStartTime);
        serializedGame.put("doingTurn", doingTurn);

        return serializedGame;
    }

    public static Game deserialize(DocumentSnapshot document) {
        //UUID id = UUID.fromString(document.getId());
        logger.info(document.getId());
        String token = document.getString("token");
        logger.info(token);
        HashMap<String, Object> serializedPlayers = (HashMap<String, Object>) document.get("players");
        for(String key : serializedPlayers.keySet()) {
            HashMap<String, Object> serializedPlayer = (HashMap<String, Object>) serializedPlayers.get(key);
            logger.info(serializedPlayer.get("selected").toString());
            ArrayList<Object> cardList = (ArrayList<Object>) serializedPlayer.get("cards");
            logger.info(cardList.toString());
            for(Object cardObject : cardList) {

                HashMap<String, Object> serializedCard = (HashMap<String, Object>) cardObject;
                logger.info(serializedCard.toString());
                String type = (String) serializedCard.get("type");
                Long value = (Long) serializedCard.get("value");
                logger.info(type);
                logger.info(String.valueOf(value));
            }
        }

        return null;
    }

    public UUID getId() {
        return id;
    }

    public ArrayList<Team> getTeams() {
        return teams;
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

    }

    @Override
    public void unregisterObserver(View v) {

    }

    @Override
    public void notifyObservers() {

    }
}
