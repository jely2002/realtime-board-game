package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import nl.hsleiden.ipsene.exceptions.GameNotFoundException;
import nl.hsleiden.ipsene.exceptions.ServerConnectionException;
import nl.hsleiden.ipsene.firebase.FirebaseService;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class GameController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class.getName());

    static GameController gameController;
    private final Game game;
    private final FirebaseService firebaseService;

    private GameController(FirebaseService firebaseService) {
        this.game = new Game();
        this.firebaseService = firebaseService;
        try {
            join("56532");
        } catch(ServerConnectionException | GameNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static GameController getInstance(FirebaseService firebaseService) {
        if (gameController == null) {
            gameController = new GameController(firebaseService);
        }
        return gameController;
    }

    @Override
    public void update(DocumentSnapshot document) {
        logger.info("Received update from firebase");
        game.update(document);
    }

    @Override
    public void registerObserver(View v) {
        game.registerObserver(v);
    }

    /**
     * Join an already existing game with a token
     * @param token The token that the user has provided
     * @throws GameNotFoundException Gets thrown when no game with the given token was found
     * @throws ServerConnectionException Gets thrown when no connection with firebase could be made
     */
    public void join(String token) throws GameNotFoundException, ServerConnectionException {
        try {
            DocumentSnapshot document = firebaseService.get(token);
            if(document == null) {
                throw new GameNotFoundException("No game with token " + token + " was found");
            } else {
                game.update(document);
                firebaseService.listen(game.getToken(), this);
                // TODO Switch view to lobby (do this in view or controller?)
            }
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new ServerConnectionException();
        }
    }

    /**
     * Host/start a new empty game
     * @return Returns the token of the new game
     * @throws ServerConnectionException Gets thrown when no connection with firebase could be made
     */
    public String host() throws ServerConnectionException {
        try {
            firebaseService.set(game.getToken(), game.serialize());
            firebaseService.listen(game.getToken(), this);
            return game.getToken();
        } catch (ExecutionException | InterruptedException e) {
            throw new ServerConnectionException();
        }
    }

    public void quit() {
        Platform.exit();
    }
}
