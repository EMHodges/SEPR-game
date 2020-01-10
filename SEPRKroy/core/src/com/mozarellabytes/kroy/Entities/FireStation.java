package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import java.util.ArrayList;
import com.mozarellabytes.kroy.Utilities.SoundFX;

/**
 * FireStation is a class created when it is called from GameScreen.
 * This class contains the location and sprite of the FireStation.
 * The FireStation spawns, repairs and refills the firetrucks and
 * ensures that trucks do not collide
 */

public class FireStation {

    /**
     * X and Y co-ordinates of the FireStation's position on the game screen
     * in tiles
     */
    private final Vector2 position;

    /** The tile where new FireTrucks are spawned */
    private final Vector2 spawnTile;

    /** A tile inside the station where a truck can be repaired and refilled */
    private final Vector2 bayTile1;

    /** A second tile inside the station where a truck can be repaired and
     * refilled
     * */
    private final Vector2 bayTile2;

    /** The sprite image for the station */
    private final Texture texture;

    /** List of active fire trucks */
    private final ArrayList<FireTruck> trucks;


    /**
     * Constructs the Firestation
     *
     * @param x  x coordinate of Station in tiles (lower left point)
     * @param y  y coordinate of Station in tiles (lower left point)
     */
    public FireStation(int x, int y) {
      //  this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.spawnTile = new Vector2(x+2, y);
        this.bayTile1 = new Vector2(x, y);
        this.bayTile2 = new Vector2(x+1, y);
        this.texture = new Texture(Gdx.files.internal("sprites/station/station.png"));
        this.trucks = new ArrayList<FireTruck>();
    }

    /**
     * Creates a fire truck of type specified from FireTruckType. It signals to
     * the game state that a truck has been created and add the truck to the
     * arraylist this.truck so the game screen can iterate through all active trucks
     */
    public void spawn(FireTruck truck) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_spawn.play();
        }
        this.trucks.add(truck);
    }


    /**
     * Calls the repair and refill methods. When a truck is within the station
     * (the trucks position is the same as one of the station's bay tiles) the
     * truck will repair and refill at the same time.
     */
    public void restoreTrucks() {
        for (FireTruck truck : this.trucks) {
            if (truck.getPosition().equals(this.bayTile1) || truck.getPosition().equals(this.bayTile2)) {
                repair(truck);
                refill(truck);
            }
        }
    }

    /**
     * Increments the truck's HP until the truck's HP equals the truck's maximum
     * HP
     * @param truck
     */
    public void refill(FireTruck truck) {
        if (truck.getReserve() < truck.type.getMaxReserve()) {
            truck.refill(0.06f);
        }
    }

    /**
     * Increments the truck's reserve until the truck's reserve equals the
     * truck's maximum reserve
     * @param truck
     */
    public void repair(FireTruck truck) {
        if (truck.getHP() < truck.type.getMaxHP()) {
            truck.repair(0.04f);
        }
    }

    /**
     * Called when a truck's HP reaches 0, it removes the truck from the
     * array list of current trucks and the game screen.
     * It also signals to the game state that a truck has been destroyed
     * @param truck
     */
    public void destroyTruck(FireTruck truck) {
        this.trucks.remove(truck);
    }


    /** generate pairs of trucks **/
    /** see if can do this with an arraylist */
    public void checkForCollisions() {
        for (FireTruck truck : trucks) {
            for (FireTruck truck2 : trucks) {
                if (!(truck.equals(truck2))) {
                    checkIfTrucksCollided(truck, truck2);
                }
            }
        }
    }

    /**
     * Checks that no more than one truck occupies a tile at a time by checking trucks
     * are not moving towards each other and that a moving truck is not going to go onto
     * the same tile as a stationary truck. If two trucks are going to collide reset
     * trucks is called.
     */
    private void checkIfTrucksCollided(FireTruck truck, FireTruck truck2){
        if (!truck.trailPath.isEmpty() && !truck.getPosition().equals(spawnTile)) {
            Vector2 truck2tile = new Vector2(Math.round(truck2.getPosition().x), Math.round(truck2.getPosition().y));
            if (!truck2.trailPath.isEmpty() && truck.trailPath.first().equals(truck2.trailPath.first())) {
                truck.setCollision();
                truck2.setCollision();
                resetTruck(truck, truck2);
            } else if (truck.trailPath.first().equals(truck2tile)) {
                resetTruck(truck);
            }
        }
    }

    /** Remove the truck's path meaning the truck has position to move to so will
     * halt on it's current tile
     * @param truck
     */
    private void resetTruck(FireTruck truck){
        SoundFX.sfx_horn.play();
        truck.resetPath();
    }

    /** Resets two trucks - is called when both trucks are moving towards each other
     * It removes their paths so they halt on the tile of the collision. It then adds
     * the nearest tile to their path, the trucks move to this tile so that after the
     * collision the trucks are positioned at the centre of adjacent tiles.
     *
     * @param truck
     * @param truck2
     */
    private void resetTruck(FireTruck truck, FireTruck truck2) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_horn.play();
        }

        Vector2 hold = truck.trailPath.first();

        truck.resetPath();
        truck.addTileToPath(truck.getPosition());
        truck.addTileToPath(new Vector2 ((float)Math.floor(truck.getX()),(float)Math.floor(truck.getY())));

        truck2.resetPath();
        truck2.addTileToPath(truck2.getPosition());
        truck2.addTileToPath(hold);
    }


    /** Draws the firetruck to the gameScreen */
    public void draw(Batch mapBatch) {
        mapBatch.draw(this.getTexture(), this.getPosition().x - 1, this.getPosition().y, 5, 3);
    }

    public ArrayList<FireTruck> getTrucks() {
        return this.trucks;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public FireTruck getTruck(int i) {
        return this.trucks.get(i);
    }

    public Texture getTexture() {
        return this.texture;
    }
}
