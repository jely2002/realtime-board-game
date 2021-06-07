package nl.hsleiden.ipsene.models;

import java.util.*;
import java.util.stream.Collectors;

import nl.hsleiden.ipsene.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player implements Model, FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Player.class.getName());

  private ArrayList<Card> cards;
  /** index of the player in its team */
  private int playerIndex;

  private Team team;
  private ArrayList<Pawn> pawns;

  private int selectedPawnIndex = 0;
  private int selectedCardIndex = 0;

  private int id;

  /**
   * should not be called manually, call through Team#createPlayers
   *
   * @param team the players team
   * @param index the players index within its team
   */
  public Player(Team team, int id, int index, ArrayList<Pawn> pawns) {
    cards = new ArrayList<Card>();
    this.id = id;
    this.team = team;
    this.playerIndex = index;
    this.pawns = pawns;
    for (Pawn p : pawns) {
      p.setOwningPlayer(this);
    }
  }

  public Pawn getPawn(int pawnIndex) {
    return pawns.get(pawnIndex);
  }

  public void doTurn() {
    // just zero for now, should use a callback from the view to get the clicked pawns index or
    // something
    // todo set selected card and pawn before calling before getting the pawn and playing the card
    Pawn selectedPawn = team.getPawn(playerIndex, selectedPawnIndex);
    if (selectedPawn != null) playCard(selectedPawn);
    else {
      logger.warn("Player#doTurn failed. No pawn selected.");
    }
  }

  public void addCard(Card card) {
    if (card == null) logger.warn("card given to player with Player#addCard is null");
    cards.add(card);
  }

  private void playCard(Pawn pawn) {
    Card c = cards.get(selectedCardIndex);
    c.play(this, pawn);
    cards.remove(c);
    // removed a card so call observers
    notifyObservers();
  }

  public int getId() {
    return id;
  }

  @Override
  public void registerObserver(View v) {}

  @Override
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {}

  @Override
  public Map<String, Object> serialize() {
    List<Map<String, Object>> serializedCards = cards.stream().map(card -> card.serialize()).collect(Collectors.toList());
    LinkedHashMap<String, Object> serializedPlayer = new LinkedHashMap<>();
    serializedPlayer.put("cards", serializedCards);
    serializedPlayer.put("selected", false);
    return serializedPlayer;
  }
}
