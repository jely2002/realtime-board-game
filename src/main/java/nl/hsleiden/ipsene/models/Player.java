package nl.hsleiden.ipsene.models;

<<<<<<< HEAD
import java.util.ArrayList;

public class Player {
=======
import nl.hsleiden.ipsene.observers.PlayerObserver;
import nl.hsleiden.ipsene.observers.PlayerObservable;

import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerObservable {
>>>>>>> origin/develop
    private ArrayList<Card> cards;

    public Player() {
        cards = new ArrayList<Card>();
    }

    public void addCard() {
        // push back a card and check bounds
    }
<<<<<<< HEAD
=======

    private List<PlayerObserver> observers = new ArrayList<PlayerObserver>();

    // Add an observer to the list
    public void register(PlayerObserver plo){
        observers.add(plo);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers(){
        for (PlayerObserver plo : observers) {
            plo.update((PlayerObserver) this);
        }
    }

>>>>>>> origin/develop
}
