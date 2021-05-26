package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.LobbyObserver;
import nl.hsleiden.ipsene.observers.LobbyObservable;
import nl.hsleiden.ipsene.views.LobbyView;

import java.util.ArrayList;
import java.util.List;

public class Lobby implements LobbyObservable {

    private List<LobbyObserver> observers = new ArrayList<LobbyObserver>();

    public void register(LobbyView lobbyView) {
        this.observers.add(lobbyView);
    }

    // Add an observer to the list
    public void register(LobbyObserver lo){
        observers.add(lo);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers() {
        for (LobbyObserver lo : observers) {
            lo.update((LobbyObserver) this);
        }
    }

}
