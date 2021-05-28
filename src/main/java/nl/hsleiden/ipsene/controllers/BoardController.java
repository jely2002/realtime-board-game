package nl.hsleiden.ipsene.controllers;

public class BoardController {

    private CardController cardController;
    private TeamController teamController;
    private boolean gameHasEnded = false;

    public BoardController(int amountOfPlayers, int amountOfTeams) {
        Integer[] cards = CardController.generateDeck(amountOfPlayers);
        // todo sent cards array to firebase
        cardController = new CardController(cards);
        teamController = new TeamController();
    }
    public void doGameLoop() {
        int cardsToBeDrawn = 5;
        int roundNum = 1;
        while (gameHasEnded == false) {
            // 5, 4, 4, shuffle, repeat
            if (roundNum == 2 || roundNum == 3)
                cardsToBeDrawn = 4;
            else if (roundNum == 4)
            {
                cardsToBeDrawn = 5;
                // todo make new deck, and reshuffle cards
                // temp to avoid crash should normally be sent to firebase too... .. .
                Integer[] cards = CardController.generateDeck(4);
                cardController = new CardController(cards);
            }
            ++roundNum;

            // todo check if there are enough cards left in the deck
            teamController.setCardsToBeDrawnNextTurn(cardsToBeDrawn);
            teamController.distributeCards(cardController);
            teamController.doTurns();
            // todo more game logic stuff
            // todo check if game has ended and set gameHasEnded
        }

    }
}
