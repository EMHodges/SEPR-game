package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;

public class FireStation {
    private final GameScreen gameScreen;
    private int x,y;
    private ArrayList<FireTruck> trucks;

    public FireStation(GameScreen gameScreen, int x, int y) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.trucks = new ArrayList<FireTruck>();
    }

    public ArrayList<FireTruck> getTrucks() {
        return this.trucks;
    }

    public FireTruck getTruck(int i) {
        return this.trucks.get(i);
    }

    public void spawn(double speed, String colour) {
        this.trucks.add(new FireTruck(gameScreen, this.x, this.y, speed, colour));
    }

    public void repair(FireTruck truck) {
        if (truck.getPosition().equals(new Vector3(this.x, this.y, 0))) {
            truck.repair();
        }
    }

    public void refill(FireTruck truck) {
        if (truck.getPosition().equals(new Vector3(this.x, this.y, 0))) {
            truck.refill();
        }
    }

}
