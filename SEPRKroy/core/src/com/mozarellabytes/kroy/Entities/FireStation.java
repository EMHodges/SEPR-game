package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import java.util.ArrayList;
import com.mozarellabytes.kroy.Utilities.SoundFX;

public class FireStation {
    private final GameScreen gameScreen;

    private final int x,y;
    private final Vector2 spawnTile;
    private final Vector2 bayTile1;
    private final Vector2 bayTile2;
    private final Texture texture;

    private ArrayList<FireTruck> trucks;

    public FireStation(GameScreen gameScreen, int x, int y) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.spawnTile = new Vector2(this.x + 2, this.y);
        this.bayTile1 = new Vector2(this.x, this.y);
        this.bayTile2 = new Vector2(this.x + 1, this.y);
        this.texture = new Texture(Gdx.files.internal("sprites/station/station.png"));
        this.trucks = new ArrayList<FireTruck>();
    }

    public void spawn(FireTruckType type) {
        SoundFX.sfx_truck_spawn.play();
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

    public void restoreTrucks() {
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

    public void checkForCollisions() {
        for (FireTruck truck : trucks) {
            for (int i = 0; i < trucks.size(); i++) {
                FireTruck truck2 = trucks.get(i);
                if (!(truck.equals(truck2))) {
                    if (!truck.trailPath.isEmpty() && !truck.getPosition().equals(spawnTile)) {
                        if (!truck2.trailPath.isEmpty() && truck.trailPath.first().equals(truck2.trailPath.first())) {
                            truck.setCollision();
                            truck2.setCollision();
                            resetTrucks(truck, truck2, true);
                        } else if (truck.trailPath.first().equals(truck2.getPosition())) {
                            resetTrucks(truck, truck2, false);
                        } else if (truck.trailPath.first().equals(new Vector2(Math.round(truck2.getPosition().x),Math.round(truck2.getPosition().y)))){
                            resetTrucks(truck, truck2, false);
                        } else if (truck.trailPath.first().equals(new Vector2((float)Math.floor(truck2.getPosition().x),(float)Math.floor(truck2.getPosition().y)))){
                            resetTrucks(truck, truck2, false);
                        }
                    }
                }
            }

        }
    }

    private void resetTrucks(FireTruck truck, FireTruck truck2, boolean bothMoving) {
        SoundFX.sfx_horn.play();
        if (bothMoving) {
            Vector2 hold = truck.trailPath.first();

            truck.resetPath();
            truck.addTileToPath(truck.getPosition());
            truck.addTileToPath(new Vector2 ((float)Math.floor(truck.getX()),(float)Math.floor(truck.getY())));

            truck2.resetPath();
            truck2.addTileToPath(truck2.getPosition());
            truck2.addTileToPath(hold);
        } else {
            truck.resetPath();
        }
    }

    public void draw(Batch mapBatch) {
        mapBatch.draw(this.getTexture(), this.getPosition().x - 1, this.getPosition().y, 5, 3);
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
}
