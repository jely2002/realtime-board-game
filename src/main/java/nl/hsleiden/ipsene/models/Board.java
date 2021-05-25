package nl.hsleiden.ipsene.models;

import java.util.HashMap;

public class Board {
    public static final int STEPS_BETWEEN_TEAMS = 14;
    public static final HashMap<TeamType, Integer> boardOffset = new HashMap<TeamType, Integer>();
    static {
        boardOffset.put(TeamType.RED, 0);
        boardOffset.put(TeamType.GREEN, STEPS_BETWEEN_TEAMS);
        boardOffset.put(TeamType.BLUE, STEPS_BETWEEN_TEAMS * 2);
        boardOffset.put(TeamType.YELLOW, STEPS_BETWEEN_TEAMS * 3);
    }
}
