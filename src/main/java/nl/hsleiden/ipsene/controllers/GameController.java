package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Game;
import nl.hsleiden.ipsene.models.Player;
import nl.hsleiden.ipsene.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GameController implements Controller {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class.getName());

  private final Game game;
  private final FirebaseService firebaseService;

  public GameController(FirebaseService firebaseService, Game game) {
    this.game = game;
    this.firebaseService = firebaseService;
  }
  public Game getGame() { return game; }
  public Player getOwnPlayer() {
    return game.getPlayer(game.getOwnPlayer());
  }
  public ArrayList<Team> getTeams() {
    return game.getTeams();
  }
  public void serializeGame() {
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
