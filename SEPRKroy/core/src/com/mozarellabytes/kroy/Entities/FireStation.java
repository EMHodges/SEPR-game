package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import java.util.ArrayList;

public class FireStation {
    private final GameScreen gameScreen;
    private int x,y;
    private ArrayList<FireTruck> trucks;

    private Vector2 spawnTile;
    private Vector2 bayTile1;
    private Vector2 bayTile2;

    private String name;

    private Texture texture;

    public FireStation(GameScreen gameScreen, int x, int y) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.bayTile1 = new Vector2(this.x, this.y);
        this.bayTile2 = new Vector2(this.x + 1, this.y);
        this.spawnTile = new Vector2(this.x + 2, this.y);
        this.trucks = new ArrayList<FireTruck>();
        this.texture = new Texture(Gdx.files.internal("sprites/station_wider.png"));
        this.name = "Fire Station";
    }

    public ArrayList<FireTruck> getTrucks() {
        return this.trucks;
    }

    public Vector2 getPosition() {
        return new Vector2(this.x, this.y);
    }

    public FireTruck getTruck(int i) {
        return this.trucks.get(i);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void spawn(FireTruckType type) {
        this.trucks.add(new FireTruck(gameScreen, this.spawnTile.x, this.spawnTile.y, type ));
        gameScreen.gameState.addFireTruck();
    }

    public void repair(FireTruck truck) {
        if (truck.getHP() < truck.type.getMaxHP()) {
            truck.repair();
        }
    }

    public void refill(FireTruck truck) {
        if (truck.getReserve() < truck.type.getMaxReserve()) {
            truck.refill();
        }
    }

    public void containsTrucks() {
        for (FireTruck truck : this.trucks) {
            if (truck.getPosition().equals(this.bayTile1) || truck.getPosition().equals(this.bayTile2)) {
                refill(truck);
                repair(truck);
            }
        }
    }

    public void destroyTruck(FireTruck truck) {
        this.trucks.remove(truck);
        gameScreen.gameState.removeFireTruck();
    }

}
