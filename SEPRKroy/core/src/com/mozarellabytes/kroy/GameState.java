package com.mozarellabytes.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameState {

    private int activeFireTrucks;
    private int fortressesDestroyed;

    public GameState() {
        this.activeFireTrucks = 0;
        this.fortressesDestroyed = 0;
        // do we need this.fortressesDestroyed to also = 0
    }

    public void addFireTruck() {
        this.activeFireTrucks++;
    }

    public void removeFireTruck() {
        this.activeFireTrucks--;
    }

    public void addFortress() {
        this.fortressesDestroyed++;
    }

    public boolean checkWin() {
        Gdx.app.log("fortressesDestroyed", String.valueOf(fortressesDestroyed));
        if (fortressesDestroyed == 3) {
            return true;
        }
        return false;
    }
    public boolean checkLose() {
        if (this.activeFireTrucks == 0) {
            return true;
        }
        return false;
    }

    public void removeFortress() {
        fortressesDestroyed ++;

    }
}
