package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.CardType;
import nl.hsleiden.ipsene.models.Deck;
import nl.hsleiden.ipsene.views.View;

public class BoardController implements Controller {

  private Deck cards;
  private TeamController teamController;
  private boolean gameHasEnded = false;
  private final int AMOUNT_OF_PLAYERS;

  public BoardController(int amountOfPlayers, int amountOfTeams) {
    AMOUNT_OF_PLAYERS = amountOfPlayers;

    // todo sent cards array to firebase
    teamController = new TeamController();
  }

  public void doGameLoop() {
    int cardsToBeDrawn = 5;
    int roundNum = 1;
    cards = CardController.generateDeck(AMOUNT_OF_PLAYERS);
    while (gameHasEnded == false) {
      // 5, 4, 4, shuffle, repeat
      if (roundNum == 2 || roundNum == 3) cardsToBeDrawn = 4;
      else if (roundNum == 4) {
        cardsToBeDrawn = 5;
        roundNum = 1;
        // todo make new deck, and reshuffle cards
        // temp to avoid crash should normally be sent to firebase too... .. .
        cards = CardController.generateDeck(AMOUNT_OF_PLAYERS);
      }
      ++roundNum;
      // todo check if there are enough cards left in the deck
      teamController.setCardsToBeDrawnNextTurn(cardsToBeDrawn);
      teamController.distributeCards(cards);
      teamController.doTurns();
      // todo more game logic stuff
      // todo check if game has ended and set gameHasEnded
      // gameHasEnded = true;
    }
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
