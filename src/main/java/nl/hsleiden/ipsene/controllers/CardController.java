package nl.hsleiden.ipsene.controllers;

import nl.hsleiden.ipsene.models.Card;

import java.util.*;

public class CardController {

    private ArrayList<Card> deck = new ArrayList<Card>();
    // all possible values for nCards filled by generateDeck, slowly emptied over the course of the game
    private static ArrayList<Integer> nCardValues = new ArrayList<Integer>();
    /** generate a shuffled deck of integers, used to create cards
     * @param amountOfPlayers the amount of players in the game
     * @return a shuffled deck of integers corresponding to Cards
     */
    public static Integer[] generateDeck(int amountOfPlayers) {
        fillNCardValues(amountOfPlayers);

        // each different card appears once for every player
        Integer[] cards = new Integer[6 * amountOfPlayers];
        // fill the deck
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < amountOfPlayers; j++) {
                cards[index] = i;
                ++index;
            }
        }
        // shuffle
        List<Integer> shuffled = Arrays.asList(cards);
        Collections.shuffle(shuffled);
        shuffled.toArray(cards);

        return cards;
    }
    /**
     * @param deck the deck of cards, must be generated with the static Card.generateDeck
     */
    public CardController(Integer[] deck) {
        // copy our deck and generate cards
        for (int i = 0; i < deck.length; i++) {
            this.deck.add(new Card(deck[i]));
        }
    }

    /** take a card from the top of the deck
     * @return the top card or null if deck is empty
     */
    public Card drawCard() {
        if (deck.size() != 0)
            return deck.remove(deck.size() - 1);
        else
            return null;
    }

    /** internally called by Card to determine the value of the 'step' variable
     * @return a value to be used by an nCard for its step amount, 0 if all values have been taken
     */
    public static int getNCardStepValue() {
        int val = 0;
        if (nCardValues.size() > 0) {
            val = nCardValues.remove(nCardValues.size() - 1);
        }
        return val;
    }

    /** fills nCardValues with possible values for n and shuffles it
     * @param amountOfPlayers the amount of players in the game
     */
    private static void fillNCardValues(int amountOfPlayers) {
        int[] possible = {2, 3, 5, 6, 8, 9, 10, 12};
        for (int i = 0; i < amountOfPlayers; i++) {
            for (int j = 0; j < possible.length; j++) {
                nCardValues.add(possible[j]);
            }
        }
        Collections.shuffle(nCardValues);
    }

}