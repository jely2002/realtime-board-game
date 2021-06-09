package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import java.util.stream.Collectors;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player implements FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Player.class.getName());

  private final Game game;

  private ArrayList<Card> cards;
  /** index of the player in its team */
  private int playerIndex;

  private Team team;
  private ArrayList<Pawn> pawns;

  private int selectedPawnIndex = 0;
  private int selectedCardIndex = 0;

  private int id;
  private boolean available;

  /**
   * should not be called manually, call through Team#createPlayers
   *
   * @param team the players team
   * @param index the players index within its team
   */
  public Player(Team team, int id, int index, ArrayList<Pawn> pawns, Game game) {
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
        cards.stream().map(card -> card.serialize()).collect(Collectors.toList());
    LinkedHashMap<String, Object> serializedPlayer = new LinkedHashMap<>();
    serializedPlayer.put("cards", serializedCards);
    serializedPlayer.put("pawns", serializedPawns);
    serializedPlayer.put("selected", !available);
    return serializedPlayer;
  }

  @Override
  public void update(DocumentSnapshot document) {
    HashMap<String, Object> players = (HashMap<String, Object>) document.get("players");
    HashMap<String, Object> player = (HashMap<String, Object>) players.get(String.valueOf(id));
    available = !(boolean) player.get("selected");
    logger.info("Updated, player {}", available);
    cards.forEach(card -> card.update(document));
    pawns.forEach(card -> card.update(document));
  }
}
