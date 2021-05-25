package nl.hsleiden.ipsene.models;

public class Pawn {
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
}
