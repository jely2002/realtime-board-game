package nl.hsleiden.ipsene.controllers;

import nl.hsleiden.ipsene.models.CardType;

public class BoardController {

  private CardController cardController;
  private TeamController teamController;
  private boolean gameHasEnded = false;
  private final int AMOUNT_OF_PLAYERS;

  public BoardController(int amountOfPlayers, int amountOfTeams) {
    AMOUNT_OF_PLAYERS = amountOfPlayers;
    generateDeck();
    // todo sent cards array to firebase
    teamController = new TeamController();
  }

  public void doGameLoop() {
    int cardsToBeDrawn = 5;
    int roundNum = 1;
    while (gameHasEnded == false) {
      // 5, 4, 4, shuffle, repeat
      if (roundNum == 2 || roundNum == 3) cardsToBeDrawn = 4;
      else if (roundNum == 4) {
        cardsToBeDrawn = 5;
        roundNum = 1;
        // todo make new deck, and reshuffle cards
        // temp to avoid crash should normally be sent to firebase too... .. .
        generateDeck();
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

  private void generateDeck() {
    CardType[] cards = CardController.generateDeck(AMOUNT_OF_PLAYERS);
    cardController = new CardController(cards);
  }
}
