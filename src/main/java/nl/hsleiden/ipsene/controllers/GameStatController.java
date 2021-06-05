package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.GameStat;
import nl.hsleiden.ipsene.views.GameStatView;

public class GameStatController {

    static GameStatController gameStatController = null;
    GameStat gameStat;

    private GameStatController(){
        gameStat = new GameStat();
    }

    public void update(DocumentSnapshot ds) {

    }

    public void registerObserver(GameStatView v) {
        gameStat.registerObserver(v);
    }

    public static GameStatController getInstance() {
        if (gameStatController == null) {
            gameStatController = new GameStatController();
        }
        return gameStatController;
    }

    public void startTurnTimer() {
        gameStat.startTurnTimer();
    }
}
