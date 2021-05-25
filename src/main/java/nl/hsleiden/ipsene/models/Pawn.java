package nl.hsleiden.ipsene.models;

<<<<<<< HEAD
public class Pawn {
=======
import nl.hsleiden.ipsene.observers.PawnObserver;
import nl.hsleiden.ipsene.observers.PawnObservable;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements PawnObservable {
>>>>>>> origin/develop
    private int boardPos;
    private final TeamType team;
    private final int pawnNum;


    /**
     * @param team - the 'type' of the team, from enum TeamType used to get team info and calculate pos
     * @param pawnNum - the number of the pawn, acts as an id and is used to determine initial board position
     */
    public Pawn(TeamType team, int pawnNum) {
        this.team = team;
        this.pawnNum = pawnNum;
        boardPos = pawnNum;
    }
    public int getAbsoluteBoardPos() {
        return Board.boardOffset.get(team) + boardPos;
    }
    public int getRelativeBoardPos() {
        return boardPos;
    }
    public void setRelativeBoardpos(int pos) {
        boardPos = pos;
    }
    public void addRelativeBoardPos(int amount) {
        setRelativeBoardpos(boardPos + amount);
    }
<<<<<<< HEAD
=======

    private List<PawnObserver> observers = new ArrayList<PawnObserver>();

    // Add an observer to the list
    public void register(PawnObserver pao){
        observers.add(pao);
    }
    // Signal all observers that something has changed.
    // Also send <<this>> object to the observers.
    public void notifyAllObservers(){
        for (PawnObserver pao : observers) {
            pao.update((PawnObserver) this);
        }
    }
>>>>>>> origin/develop
}
