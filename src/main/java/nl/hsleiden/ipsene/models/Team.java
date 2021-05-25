package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.TeamObserver;
import nl.hsleiden.ipsene.observers.TeamObservable;

import java.util.ArrayList;
import java.util.List;

public class Team implements TeamObservable {
    private Pawn[][] pawns;
    public static final int PAWNS_PER_PLAYER = 2; // idk
    public static final int PLAYERS_PER_TEAM = 2;


    /**
     * @param teamtype the type of the team, given to the pawns created in the constructor
     */
    public Team(TeamType teamtype) {
        pawns = new Pawn[PLAYERS_PER_TEAM][PAWNS_PER_PLAYER];
        int pawnNum = 0;
        for (int i = 0; i < PAWNS_PER_PLAYER; i++) {
            for (int j = 0; j < PLAYERS_PER_TEAM; j++) {
                pawns[i][j] = new Pawn(teamtype, pawnNum);
                ++pawnNum;
            }
        }
    }

    private List<TeamObserver> observers = new ArrayList<TeamObserver>();

    // Add an observer to the list
    public void register(TeamObserver to){
        observers.add(to);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers(){
        for (TeamObserver to : observers) {
            to.update((TeamObserver) this);
        }
    }

}
