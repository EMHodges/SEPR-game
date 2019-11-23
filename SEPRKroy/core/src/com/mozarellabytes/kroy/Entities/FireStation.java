package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireStation {
    private final GameScreen gameScreen;
    private int x,y;
    private ArrayList<FireTruck> trucks;

    String[] truckColours = new String[] {"red", "blue"};
    private double upperSpeed = 0.8;
    private double lowerSpeed = 0.2;


    Random r = new Random();

    public FireStation(GameScreen gameScreen, int x, int y) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.trucks = new ArrayList<FireTruck>();
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

    // added randomly generating the firetrucks speed and colour, not sure if colour is helpful but speed might be
    public void spawn() {

        double speed = Math.random() * (upperSpeed - lowerSpeed) + lowerSpeed;
        int index = new Random().nextInt(truckColours.length);
        String colour = (truckColours[index]);
        this.trucks.add(new FireTruck(gameScreen, this.x, this.y, speed, colour));
    }

    public void repair(FireTruck truck) {
        if (truck.getPosition().equals(new Vector2(this.x, this.y))) {
            if (truck.getHP() < truck.getMaxHP()) {
                truck.refill();
                Gdx.app.log("Repair", "Truck is being repaired: " + truck.getHP() + "/"+ truck.getMaxHP());
            }
        }
    }

    public void refill(FireTruck truck) {
        if (truck.getPosition().equals(new Vector2(this.x, this.y))) {
            if (truck.getReserve() < truck.getMaxReserve()) {
                truck.refill();
                Gdx.app.log("Refill", "Truck is being refilled: " + truck.getReserve() + "/"+ truck.getMaxReserve());
            }
        }
    }

    public void checkTrucks() {
        for (FireTruck truck : this.trucks) {
            if (truck.getPosition().equals(this.getPosition())) {
                refill(truck);
                repair(truck);
            }
        }
    }
}
