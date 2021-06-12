package nl.hsleiden.ipsene.controllers;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController implements Controller {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class.getName());

  private final Game game;
  private final FirebaseService firebaseService;
  private final int MAX_TURN_TIME = 100;

  public GameController(FirebaseService firebaseService, Game game) {
    this.game = game;
    this.firebaseService = firebaseService;
    game.setGameHasStarted(true);
    firebaseService.listen(game.getToken(), this);
  }

  public FirebaseService getFirebaseService() {
    return firebaseService;
  }

  private Player getOwnPlayer() {
    return game.getPlayer(game.getOwnPlayer() - 1);
  }

  public boolean setOwnPlayerClickedCardIndex(int index) {
    Player ourPlayer = getOwnPlayer();
    if (index < ourPlayer.getCards().size()) {
      ourPlayer.setSelectedCardIndex(index);
      return true;
    }
    return false;
  }

  public boolean hasOwnPlayerPassed() {
    return getOwnPlayer().hasPassed();
  }

  public boolean isPlayerOwnPlayer(Team team, int index) {
    Player p = team.getPlayer(index);
    return p.equals(getOwnPlayer());
  }

  public final ArrayList<Card> getOwnPlayerCards() {
    return getOwnPlayer().getCards();
  }

  public boolean isOwnPlayerCurrentPlayer() {
    return getIdCurrentPlayer() == getOwnPlayer().getId();
  }
  public int getOwnPlayerId() { return getOwnPlayer().getId(); }

  public void setOwnPlayerSelectedPawnIndex(int index) {
    getOwnPlayer().setSelectedPawnIndex(index);
  }

  public boolean doOwnPlayerTurn() {
    return getOwnPlayer().doTurn();
  }

  public Integer getIdCurrentPlayer() {
    return game.getDoingTurn();
  }

  public final Pawn[] getOwnPlayerPawns() {
    return getOwnPlayer().getPawns().toArray(new Pawn[0]);
  }

  public final ArrayList<Team> getTeams() {
    return game.getTeams();
  }

  public int getRound() {
    return game.getRound();
  }
  public boolean doesOwnPlayerHaveCards() {
    return getOwnPlayer().getCards().size() != 0;
  }
  /**
   * Adds 1 to the id of the current player or wraps around when the highest value is reached. If
   * there are no players left who have cards, we go to the next round.
   */
  public void increasePlayerCounter() {
    int nextPlayer = game.getDoingTurn() + 1;
    int highestPlayer = (Team.PLAYERS_PER_TEAM * Game.AMOUNT_OF_TEAMS) - 1;
    game.setDoingTurnPlayer((nextPlayer <= highestPlayer) ? nextPlayer : 0);
    if (game.amountOfPlayersWithCards() == 0) {
      game.advanceRound();
    }
  }

  public int getTimeLeft() {
    Timestamp startTime = game.getTurnStartTime();
    if (startTime == null) {
      startTime = Timestamp.now();
    }
    int secondsPast = startTime.compareTo(Timestamp.now());
    return MAX_TURN_TIME - secondsPast;
  }

  /** Remove all cards from the player and end turn. */
  public void passTurn() {
    getOwnPlayer().passTurn();
    advanceTurn();
  }
  public void advanceTurn() {
    increasePlayerCounter();
    serialize();
  }
  public final Pawn getOwnPlayerPawn(int pawn) {
    return getOwnPlayer().getPawn(pawn);
  }

  public void serialize() {
    try {
      firebaseService.set(game.getToken(), game.serialize());
    } catch (ExecutionException | InterruptedException e) {
      logger.error(e.getMessage(), e);
    }
  }



  public void clickPawn(boolean cardSelected, double x, double y) {
    if (cardSelected) {
      Pawn closestPawn = getPawnClosestToPoint(x, y);
      setOwnPlayerSelectedPawnIndex(closestPawn.getPawnNumber());
      // if turn was successful
      if (doOwnPlayerTurn()) {
        advanceTurn();
      }
    }
  }

  private Pawn getPawnClosestToPoint(double closestPawnDistance, double pawnDistance) {
    Pawn closestPawn = getOwnPlayerPawn(0);
    // get the closest pawn to our click position
    for (int i = 1; i < Team.PAWNS_PER_PLAYER; i++) {
      Pawn p = getOwnPlayerPawn(i);
      closestPawn = (closestPawnDistance < pawnDistance) ? closestPawn : p;
    }
    return closestPawn;
  }

  public void backToMainMenu() {
    game.backToMainMenu();
  }

  public final ArrayList<Player> getAllPlayers() {
    return game.getAllPlayers();
  }
  int i = 0;
  @Override
  public void update(DocumentSnapshot document) {
    logger.info("Received update from firebase"); // TODO Remove in production
    game.update(document);
    // if our player has passed his turn skip the turn
  }

  @Override
  public void registerObserver(View v) {
    game.registerObserver(v);
    getOwnPlayer().registerObserver(v);
  }

  public void unregisterObserver(View v) {
    game.unregisterObserver(v);
    getOwnPlayer().unregisterObserver(v);
  }
}
