package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Game;
import nl.hsleiden.ipsene.models.Pawn;
import nl.hsleiden.ipsene.models.Player;
import nl.hsleiden.ipsene.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController implements Controller {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class.getName());

  private final Game game;
  private final FirebaseService firebaseService;

  public GameController(FirebaseService firebaseService, Game game) {
    this.game = game;
    this.firebaseService = firebaseService;
  }

  public Game getGame() {
    return game;
  }

  public Player getOwnPlayer() {
    return game.getPlayer(game.getOwnPlayer() - 1);
  }

  public Integer getIdCurrentPlayer() { return game.getDoingTurn(); }

  public ArrayList<Team> getTeams() {
    return game.getTeams();
  }

  public int getRound() { return game.getRound(); }

  /**
   * @param pawnNumber sets the selected pawn in our own player, the calls Player#doTurn, increases the player counter and sents to firebase
   */
  public boolean doTurn(int pawnNumber) {
    if (getOwnPlayer().isFirstPawnSelected()) {
      getOwnPlayer().setSecondSelectedPawnIndex(pawnNumber);
    }
    else {
      getOwnPlayer().setSelectedPawnIndex(pawnNumber);
    }
    return getOwnPlayer().doTurn();
  }

  /**
   * adds 1 to the id of the current player or wraps around when the highst value is reached
   * if the highst value is reached redistributes cards and advance round
   */
  public void increasePlayerCounter() {
    int nextPlayer = game.getDoingTurn() + 1;
    int highestPlayer = (Team.PLAYERS_PER_TEAM * Game.AMOUNT_OF_TEAMS) - 1;
    game.setDoingTurnPlayer((nextPlayer <= highestPlayer) ? nextPlayer : 0);
    if (game.getDoingTurn() == 0) {
      game.advanceRound();
    }
  }
  public Pawn getOwnPlayerPawn(int pawn) {
    return getOwnPlayer().getPawn(pawn);
  }
  public void serialize() {
    try {
      firebaseService.set(game.getToken(), game.serialize());
    } catch (ExecutionException | InterruptedException e) {
      logger.error("execution/interrupt exception", e);
    }
  }

  @Override
  public void update(DocumentSnapshot document) {
    logger.info("Received update from firebase"); // TODO Remove in production
    game.update(document);
  }

  @Override
  public void registerObserver(View v) {
    game.registerObserver(v);
  }
}
