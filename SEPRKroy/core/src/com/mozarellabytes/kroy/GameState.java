package com.mozarellabytes.kroy;

import com.badlogic.gdx.Game;

public class GameState {

    private int activeFireTrucks;
    private int fortressesDestroyed;

    public GameState() {
        this.activeFireTrucks = 0;

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
        if (fortressesDestroyed == 10) {
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

}
