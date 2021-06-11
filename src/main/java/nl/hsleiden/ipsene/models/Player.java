package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import java.util.stream.Collectors;
import nl.hsleiden.ipsene.firebase.Firebase;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player implements FirebaseSerializable<Map<String, Object>>, Model {

  private static final Logger logger = LoggerFactory.getLogger(Player.class.getName());

  private final Game game;

  private ArrayList<Card> cards;
  /** index of the player in its team */
  private int playerIndex;

  private Team team;
  private ArrayList<Pawn> pawns;

  private int selectedPawnIndex = 0;
  private int selectedPawnIndex2 = -1;
  private int selectedCardIndex = 0;

  private int id;
  private boolean available;
  private PlayerColour colour;

  /**
   * should not be called manually, call through Team#createPlayers
   *
   * @param team the players team
   * @param index the players index within its team
   */
  public Player(
      Team team, PlayerColour colour, int id, int index, ArrayList<Pawn> pawns, Game game) {
    this.colour = colour;
    cards = new ArrayList<Card>();
    this.game = game;
    this.id = id;
    this.team = team;
    this.playerIndex = index;
    this.pawns = pawns;
    this.available = true;
    for (Pawn p : pawns) {
      p.setOwningPlayer(this);
    }
  }

  public boolean equals(Player other) {
    return (team.teamIndex == other.team.teamIndex && id == other.id);
  }

  /**
   * sets the pawn to be moved by the card
   *
   * @param i the index of the pawn
   */
  public void setSelectedPawnIndex(int i) {
    System.out.println("first pawn selected");
    selectedPawnIndex = i;
  }

  public void setSecondSelectedPawnIndex(int i) {
    System.out.println("second pawn selected");

    if (cards.get(selectedCardIndex).getType().isTwoPawnCard()) selectedPawnIndex2 = i;
  }

  public void setSelectedCardIndex(int i) {
    for (Card c : cards) {
      c.setIsSelected(false);
    }
    cards.get(i).setIsSelected(true);
    selectedCardIndex = i;
  }

  public Pawn getPawn(int pawnIndex) {
    return pawns.get(pawnIndex);
  }

  public final ArrayList<Pawn> getPawns() {
    return pawns;
  }

  public void emptyCards() {
    cards.clear();
  }

  public boolean doTurn() {
    // if we need to select two pawns return false if the second pawn was not selected
    if (cards.get(selectedCardIndex).getType().isTwoPawnCard()) {
      if (selectedPawnIndex2 == -1) return false;
    }
    System.out.println("do turn");
    // if the pawn we play this on is not out of its pool yet
    if (cards.get(selectedCardIndex).getType() != CardType.SPAWN) {
      if (!getSelectedPawn(true).isOutOfPool()) return false;
    }
    playCard();
    return true;
  }

  public boolean isFirstPawnSelected() {
    return selectedPawnIndex != -1;
  }

  public Pawn getSelectedPawn(boolean firstPawn) {
    int index = (firstPawn) ? selectedPawnIndex : selectedPawnIndex2;
    return team.getPawn(playerIndex, index);
  }

  public void addCard(Card card) {
    if (card == null) logger.warn("card given to player with Player#addCard is null");
    cards.add(card);
  }

  private void playCard() {
    if (selectedCardIndex != -1) {
      Card c = cards.get(selectedCardIndex);
      c.play(this);
      cards.remove(selectedCardIndex);
    }
    selectedCardIndex = -1;
    notifyObservers();
  }

  public ArrayList<Card> getCards() {
    return cards;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean selected) {
    this.available = selected;
  }

  public int getId() {
    return id;
  }

  @Override
  public Map<String, Object> serialize() {
    List<Map<String, Object>> serializedCards =
        cards.stream().map(card -> card.serialize()).collect(Collectors.toList());

    List<Map<String, Object>> serializedPawns =
        pawns.stream().map(pawn -> pawn.serialize()).collect(Collectors.toList());

    LinkedHashMap<String, Object> serializedPlayer = new LinkedHashMap<>();
    serializedPlayer.put(Firebase.CARD_FIELD_NAME, serializedCards);
    serializedPlayer.put("pawns", serializedPawns);
    serializedPlayer.put("selected", !available);
    return serializedPlayer;
  }

  @Override
  public void update(DocumentSnapshot document) {
    HashMap<String, HashMap<String, Object>> serializedPlayers =
        (HashMap<String, HashMap<String, Object>>) document.get("players");

    HashMap<String, Object> ourPlayer = serializedPlayers.get(String.valueOf(getId()));
    available = !(boolean) ourPlayer.get("selected");
    ArrayList<HashMap<String, Object>> pawns =
        (ArrayList<HashMap<String, Object>>) ourPlayer.get("pawns");
    ArrayList<HashMap<String, Object>> cards =
        (ArrayList<HashMap<String, Object>>) ourPlayer.get("cards");
    // update pawns
    for (int i = 0; i < pawns.size(); i++) {
      HashMap<String, Object> pawn = pawns.get(i);
      this.pawns.get(i).update((int) (long) pawn.get("location"));
    }
    // update cards
    this.cards.clear();
    for (HashMap<String, Object> card : cards) {
      CardType cardType = CardType.valueOf((String) card.get("type"));
      int step = (int) (long) card.get("value");
      this.cards.add(new Card(cardType, step));
    }
    // unselect pawns
    selectedPawnIndex = -1;
    selectedPawnIndex2 = -1;
  }

  private final ArrayList<View> observers = new ArrayList<>();

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
    for (View v : observers) {
      v.update();
    }
  }
}
