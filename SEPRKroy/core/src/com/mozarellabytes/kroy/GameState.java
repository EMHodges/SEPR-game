package com.mozarellabytes.kroy;

import com.badlogic.gdx.Gdx;
import com.mozarellabytes.kroy.Utilities.ScreenHandler;

public class GameState {

    private int activeFireTrucks;
    private int fortressesDestroyed;

    public GameState() {
        this.activeFireTrucks = 0;
        this.fortressesDestroyed = 0;
    }

    public void addFireTruck() {
        this.activeFireTrucks++;
        Gdx.app.log("Active", String.valueOf(this.activeFireTrucks));
    }

    public void removeFireTruck() {
        this.activeFireTrucks--;
        Gdx.app.log("Remove", String.valueOf(this.activeFireTrucks));
    }

    public void addFortress() {
        this.fortressesDestroyed++;
    }

    public void hasGameEnded(Kroy game) {
        if (fortressesDestroyed == 3) {
            endGame(true, game);
        } else if (this.activeFireTrucks == 0) {
            endGame(false, game);
        }
    }

    public void endGame(Boolean playerWon, Kroy game){
        if (playerWon) {
            ScreenHandler.ToGameOverScreen(game, true);
        } else {
            ScreenHandler.ToGameOverScreen(game, false);
        }
    }


}
