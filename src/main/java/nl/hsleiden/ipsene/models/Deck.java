package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.controllers.CardController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();
    // all possible values for nCards filled by generateDeck, slowly emptied over the course of the
    // game
    private ArrayList<Integer> nCardDeck = new ArrayList<Integer>();
    // all possible values for n cards
    private static final int[] POSSIBLE_N_CARDS = {2, 3, 5, 6, 8, 9, 10, 12};

    private Card[] generateDeck(int amountOfPlayers, ArrayList<Integer> nCardDeck) {

        // each different card appears once for every player, nCards are added separately
        final int AMOUNT_NORMAL_CARDS = CardType.values().length;
        final int TOTAL_AMOUNT_OF_CARDS =
                (AMOUNT_NORMAL_CARDS * amountOfPlayers) + (POSSIBLE_N_CARDS.length * amountOfPlayers);
        Card[] cards = new Card[TOTAL_AMOUNT_OF_CARDS];

        int index = 0;
        // add normal cards
        for (int i = 0; i < AMOUNT_NORMAL_CARDS; i++) {
            for (int j = 0; j < amountOfPlayers; j++) {
                cards[index] = new Card(CardType.get(i), CardType.get(i).getSteps());
                ++index;
            }
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

    private ArrayList<Integer> generateNCardDeck(int amountOfPlayers) {
        ArrayList<Integer> nDeck = new ArrayList<>();
        for (int i = 0; i < amountOfPlayers; i++) {
            for (int j = 0; j < POSSIBLE_N_CARDS.length; j++) {
                nDeck.add(POSSIBLE_N_CARDS[j]);
            }
        }
        Collections.shuffle(nCardDeck);
        return nDeck;
    }
    public Deck(int amountOfPlayers) {
        nCardDeck = generateNCardDeck(amountOfPlayers);
        cards = new ArrayList<>(Arrays.asList(generateDeck(amountOfPlayers, nCardDeck)));
    }
    /**
     * take a card from the top of the deck
     *
     * @return the top card or null if deck is empty
     */
    public Card drawCard() {
        if (cards.size() != 0) return cards.remove(cards.size() - 1);
        else return null;
    }

    public int getAmountOfCardsInDeck() {
        return cards.size();
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

}
