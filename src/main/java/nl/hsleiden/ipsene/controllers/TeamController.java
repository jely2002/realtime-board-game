package nl.hsleiden.ipsene.controllers;

import nl.hsleiden.ipsene.models.Team;
import nl.hsleiden.ipsene.models.TeamType;

public class TeamController {
    public Team[] teams;
    public static final int AMOUNT_OF_TEAMS = 2;
    private int cardsPerPlayerNextRound = 0;

    public TeamController() {
        teams = new Team[AMOUNT_OF_TEAMS];
        TeamType[] types = {TeamType.RED, TeamType.GREEN, TeamType.BLUE, TeamType.YELLOW};
        for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
            teams[i] = new Team(types[i]);
        }
    }
    public void doTurns() {
        for (int i = 0; i < teams.length; i++) {
            teams[i].doTurn();
        }
    }
    public void distributeCards(CardController controller) {
        for (int i = 0; i < teams.length; i++) {
            teams[i].distributeCards(cardsPerPlayerNextRound, controller);
        }
    }
    public void setCardsToBeDrawnNextTurn(int amount) {
        cardsPerPlayerNextRound = amount;
    }

}
