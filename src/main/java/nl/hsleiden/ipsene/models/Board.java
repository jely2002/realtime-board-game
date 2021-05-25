package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.BoardObserver;
import nl.hsleiden.ipsene.observers.BoardObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board implements BoardObservable {
    public static final int STEPS_BETWEEN_TEAMS = 14;
    public static final HashMap<TeamType, Integer> boardOffset = new HashMap<TeamType, Integer>();



    static {
        boardOffset.put(TeamType.RED, 0);
        boardOffset.put(TeamType.GREEN, STEPS_BETWEEN_TEAMS);
        boardOffset.put(TeamType.BLUE, STEPS_BETWEEN_TEAMS * 2);
        boardOffset.put(TeamType.YELLOW, STEPS_BETWEEN_TEAMS * 3);
    }

    private List<BoardObserver> observers = new ArrayList<BoardObserver>();

    // Add an observer to the list
    public void register(BoardObserver bo){
        observers.add(bo);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers(){
        for (BoardObserver bo : observers) {
            bo.update(this);
        }
    }
}
