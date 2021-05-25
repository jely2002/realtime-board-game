package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.CardObservable;
import nl.hsleiden.ipsene.observers.CardObserver;

import java.util.ArrayList;

interface Playable {
    void play(Player player, Pawn pawn, Card card);
}

public class Card implements CardObservable {

    public final int steps;

    private Playable onPlay;
    private int type;

    /** binds the appropriate method to be called when Card.play is called and determines the correct steps to use
     *      * types:
     *      *   "spawn":        0
     *      *   "sub":          1
     *      *   "spawn_step_1": 2
     *      *   "step_7":       3
     *      *   "step_4":       4
     *      *   "step_n":       5
     * @param type the type of card as an integer
     */
    public Card(int type, int steps) {
        this.type = type;
        this.steps = steps;
        onPlay = onPlayActions[type];
    }

    /** calls the appropriate method for this card to be played
     * @param player the player playing this card
     * @param pawn the pawn the card was used on
     */
    public void play(Player player, Pawn pawn) {
        onPlay.play(player, pawn, this);
    }

    private static void playSpawnCard(Player player, Pawn pawn, Card card) {
        System.out.println("spawn");
    }
    private static void playSubCard(Player player, Pawn pawn, Card card) {
        System.out.println("sub");

    }
    private static void playSpawnStep1Card(Player player, Pawn pawn, Card card) {
        System.out.println("step1");

    }
    private static void playStep7Card(Player player, Pawn pawn, Card card) {
        System.out.println("step7");

    }
    private static void playStep4Card(Player player, Pawn pawn, Card card) {
        System.out.println("step4");

    }
    private static void playStepNCard(Player player, Pawn pawn, Card card) {
        System.out.println("n val: " + card.steps);

    }

    /**
     * types:
     *   "spawn":        0
     *   "sub":          1
     *   "spawn_step_1": 2
     *   "step_7":       3
     *   "step_4":       4
     *   "step_n":       5
     */
    private static final Playable[] onPlayActions = new Playable[6];
    static {
        onPlayActions[0] = Card::playSpawnCard;
        onPlayActions[1] = Card::playSubCard;
        onPlayActions[2] = Card::playSpawnStep1Card;
        onPlayActions[3] = Card::playStep7Card;
        onPlayActions[4] = Card::playStep4Card;
        onPlayActions[5] = Card::playStepNCard;
    }

    private ArrayList<CardObserver> observers = new ArrayList<CardObserver>();

    // Add an observer to the list
    public void register(CardObserver co){
        observers.add(co);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers() {
        for (CardObserver co : observers) {
            co.update((CardObserver) this);
        }
    }
}


