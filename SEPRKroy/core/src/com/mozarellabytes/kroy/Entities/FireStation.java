package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireStation {
    private final GameScreen gameScreen;
    private int x,y;
    private ArrayList<FireTruck> trucks;

    private double upperSpeed = 0.8;
    private double lowerSpeed = 0.2;

    private Texture texture;


    Random r = new Random();

    public FireStation(GameScreen gameScreen, int x, int y) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.trucks = new ArrayList<FireTruck>();
        this.texture = new Texture(Gdx.files.internal("sprites/station.png"));
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

    // added randomly generating the firetrucks speed // on second thoughts randomly generating the colour isn't too helpful
    public void spawn(String colour) {
        double speed = Math.random() * (upperSpeed - lowerSpeed) + lowerSpeed;
        this.trucks.add(new FireTruck(gameScreen, this.x, this.y, speed, colour));
    }

    public void repair(FireTruck truck) {
        if (truck.getHP() < truck.getMaxHP()) {
            truck.repair();
        }
    }

    public void refill(FireTruck truck) {
        if (truck.getReserve() < truck.getMaxReserve()) {
            truck.refill();
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
