package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import java.util.stream.Collectors;
import nl.hsleiden.ipsene.exceptions.OverdrawException;
import nl.hsleiden.ipsene.firebase.Firebase;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Model;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.enums.CardType;

public class Deck implements Model, FirebaseSerializable<List<Map<String, Object>>> {
  private ArrayList<Card> cards;
  private final Game game;
  private final int amountOfPlayers;
  private ArrayList<Integer> nCardDeck;
  private static final int[] POSSIBLE_N_CARDS = {2, 3, 5, 6, 8, 9, 10, 12};

  public Deck(int amountOfPlayers, Game game) {
    this.game = game;
    this.amountOfPlayers = amountOfPlayers;
    nCardDeck = generateNCardDeck(amountOfPlayers);
    cards = new ArrayList<>(Arrays.asList(generateDeck(amountOfPlayers, nCardDeck)));
  }

  /** Shuffle cards, only happens when a new deck is created currently. */
  public void regenerate() {
    nCardDeck = generateNCardDeck(amountOfPlayers);
    cards = new ArrayList<>(Arrays.asList(generateDeck(amountOfPlayers, nCardDeck)));
  }

  /**
   * Generates a deck, with 4 cars for every player for every card type.
   *
   * @param amountOfPlayers The amount of players to give cards
   * @param nCardDeck n-cards to be given to players
   * @return The generated deck as array of cards
   */
  private Card[] generateDeck(int amountOfPlayers, ArrayList<Integer> nCardDeck) {

    // each different card appears once for every player, nCards are added separately
    final int AMOUNT_NORMAL_CARDS = CardType.values().length - 1;
    // add an extra set of cards because the sub card type was removed, but the deck does need to
    // have 52 cards
    final int TOTAL_AMOUNT_OF_CARDS =
        (AMOUNT_NORMAL_CARDS * amountOfPlayers)
            + (POSSIBLE_N_CARDS.length * amountOfPlayers)
            + amountOfPlayers;
    Card[] cards = new Card[TOTAL_AMOUNT_OF_CARDS];

    int index = 0;
    // add normal cards
    for (int i = 0; i < AMOUNT_NORMAL_CARDS; i++) {
      for (int j = 0; j < amountOfPlayers; j++) {
        cards[index] = new Card(CardType.get(i), CardType.get(i).getSteps());
        ++index;
      }
    }
    // add replacement for sub cards
    for (int i = 0; i < amountOfPlayers; i++) {
      cards[index] = new Card(CardType.SPAWN, 0);
      ++index;
    }
    // add nCards
    for (int i = 0; i < amountOfPlayers * POSSIBLE_N_CARDS.length; i++) {
      cards[index] = new Card(CardType.STEP_N, getNCardStepValue());
      ++index;
    }

    // shuffle
    List<Card> shuffled = Arrays.asList(cards);
    Collections.shuffle(shuffled);
    shuffled.toArray(cards);
    return cards;
  }

  /**
   * Generate a deck with only n-cards to be used later by generateDeck
   *
   * @param amountOfPlayers The amount of players to give n-cards to
   * @return The deck of only n-cards
   */
  private ArrayList<Integer> generateNCardDeck(int amountOfPlayers) {
    ArrayList<Integer> nDeck = new ArrayList<>();
    for (int i = 0; i < amountOfPlayers; i++) {
      for (int j = 0; j < POSSIBLE_N_CARDS.length; j++) {
        nDeck.add(POSSIBLE_N_CARDS[j]);
      }
    }
    Collections.shuffle(nDeck);
    return nDeck;
  }

  /**
   * take a card from the top of the deck
   *
   * @return the top card or null if deck is empty
   */
  public Card drawCard() throws OverdrawException {
    if (cards.size() != 0) return cards.remove(cards.size() - 1);
    else throw new OverdrawException("The game attempted to draw from an empty deck");
  }

  /**
   * internally called by Card to determine the value of the 'step' variable
   *
   * @return a value to be used by an nCard for its step amount, 0 if all values have been taken
   */
  private int getNCardStepValue() {
    int val = 0;
    if (nCardDeck.size() > 0) {
      val = nCardDeck.remove(nCardDeck.size() - 1);
    }
    return val;
  }

  @Override
  public List<Map<String, Object>> serialize() {
    List<Map<String, Object>> serializedCards =
        cards.stream().map(Card::serialize).collect(Collectors.toList());
    return serializedCards;
  }

  @Override
  public void update(DocumentSnapshot document) {
    List<HashMap<String, Object>> serializedCards;
    serializedCards = (List<HashMap<String, Object>>) document.get(Firebase.CARD_FIELD_NAME);

    ArrayList<Card> newCards = new ArrayList<>();
    for (HashMap<String, Object> c : serializedCards) {
      CardType type = CardType.valueOf((String) c.get("type"));
      // comes out as 64 bit long, must be 32 bit int
      int steps = (int) (long) c.get("value");
      newCards.add(new Card(type, steps));
    }
    cards = newCards;
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
