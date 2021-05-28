package nl.hsleiden.ipsene.controllers;

import java.util.*;
import nl.hsleiden.ipsene.models.Card;

public class CardController {

  private ArrayList<Card> deck = new ArrayList<Card>();
  // all possible values for nCards filled by generateDeck, slowly emptied over the course of the
  // game
  private static ArrayList<Integer> nCardValues = new ArrayList<Integer>();
  // all possible values for n cards
  private static final int[] POSSIBLE_N_CARDS = {2, 3, 5, 6, 8, 9, 10, 12};

  /**
   * generate a shuffled deck of integers, used to create cards
   *
   * @param amountOfPlayers the amount of players in the game
   * @return a shuffled deck of integers corresponding to Cards
   */
  public static Integer[] generateDeck(int amountOfPlayers) {
    fillNCardValues(amountOfPlayers);

    // each different card appears once for every player, nCards are added separately
    final int AMOUNT_NORMAL_CARDS = 5;
    final int AMOUNT_N_CARDS = POSSIBLE_N_CARDS.length;
    int amountOfCards =
        (AMOUNT_NORMAL_CARDS * amountOfPlayers) + (AMOUNT_N_CARDS * amountOfPlayers);
    Integer[] cards = new Integer[amountOfCards];

    // fill the deck
    int index = 0;
    // add normal cards
    for (int i = 0; i < AMOUNT_NORMAL_CARDS; i++) {
      for (int j = 0; j < amountOfPlayers; j++) {
        cards[index] = i;
        ++index;
      }
    }
    // add nCards
    for (int i = 0; i < amountOfPlayers; i++) {
      for (int j = 0; j < AMOUNT_N_CARDS; j++) {
        cards[index] = 5;
        ++index;
      }
    }

    // shuffle
    List<Integer> shuffled = Arrays.asList(cards);
    Collections.shuffle(shuffled);
    shuffled.toArray(cards);

    return cards;
  }
  /** @param deck the deck of cards, must be generated with the static Card.generateDeck */
  public CardController(Integer[] deck) {
    // copy our deck and generate cards
    for (int i = 0; i < deck.length; i++) {
      int steps = 0;
      switch (deck[i]) {
        case 2:
          steps = 1;
          break;
        case 3:
          steps = 7;
          break;
        case 4:
          steps = 4;
          break;
        case 5:
          steps = CardController.getNCardStepValue();
          break;
        default:
          steps = 0;
      }
      this.deck.add(new Card(deck[i], steps));
    }
  }

  /**
   * take a card from the top of the deck
   *
   * @return the top card or null if deck is empty
   */
  public Card drawCard() {
    if (deck.size() != 0) return deck.remove(deck.size() - 1);
    else return null;
  }
  public int getAmountOfCardsInDeck() { return deck.size(); }
  /**
   * internally called by Card to determine the value of the 'step' variable
   *
   * @return a value to be used by an nCard for its step amount, 0 if all values have been taken
   */
  public static int getNCardStepValue() {
    int val = 0;
    if (nCardValues.size() > 0) {
      val = nCardValues.remove(nCardValues.size() - 1);
    }
    return val;
  }

  /**
   * fills nCardValues with possible values for n and shuffles it
   *
   * @param amountOfPlayers the amount of players in the game
   */
  private static void fillNCardValues(int amountOfPlayers) {
    for (int i = 0; i < amountOfPlayers; i++) {
      for (int j = 0; j < POSSIBLE_N_CARDS.length; j++) {
        nCardValues.add(POSSIBLE_N_CARDS[j]);
      }
    }
    Collections.shuffle(nCardValues);
  }
}
