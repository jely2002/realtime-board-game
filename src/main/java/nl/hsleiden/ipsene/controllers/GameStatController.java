package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.GameStat;
import nl.hsleiden.ipsene.views.GameStatView;
import nl.hsleiden.ipsene.views.View;

import javax.naming.ldap.Control;

public class GameStatController implements Controller {

    static GameStatController gameStatController = null;
    GameStat gameStat;

    private GameStatController(){
        gameStat = new GameStat();
    }

    public void update(DocumentSnapshot ds) {

    }

    @Override
    public void registerObserver(View v) {
        gameStat.registerObserver(v);
    }

    public static GameStatController getInstance() {
        if (gameStatController == null) {
            gameStatController = new GameStatController();
        }
        return gameStatController;
    }

    public int getCurrentTime() {
        return gameStat.getCurrentTurnTime();
    }

    public void startTurnTimer() {
        gameStat.startTurnTimer();
    }
}
