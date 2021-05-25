package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.CardObserver;
import nl.hsleiden.ipsene.observers.CardObservable;

import java.util.ArrayList;
import java.util.List;

public class Card implements CardObservable {


    private List<CardObserver> observers = new ArrayList<CardObserver>();

    // Add an observer to the list
    public void register(CardObserver co){
        observers.add(co);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers(){
        for (CardObserver co : observers) {
            co.update((CardObserver) this);
        }
    }
}
