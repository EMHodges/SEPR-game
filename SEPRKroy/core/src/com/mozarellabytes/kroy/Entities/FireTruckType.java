package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;

public enum FireTruckType {

    Speed (100, 100, 2, Color.RED, "Speed Truck", 5, 0.08f),
    Tank (100,250,1, Color.GREEN, "Tank Truck", 5, 0.08f),
    Ocean(250,100,1, Color.CYAN, "Ocean Truck", 8, 0.08f);

    private int maxReserve;
    private int maxHP;
    private int speed;
    private Color colour;
    private String name;
    private float range;
    private float AP;

    FireTruckType(int maxReserve, int maxHP, int speed, Color colour, String name, float range, float AP) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.colour = colour;
        this.name = name;
        this.range = range;
        this.AP = AP;
    }

    public int getMaxReserve(){
        return this.maxReserve;
    }

    public int getMaxHP(){
        return this.maxHP;
    }

    public int getSpeed(){
        return this.speed;
    }

    public Color getColour(){
        return this.colour;
    }

    public String getName() {
        return this.name;
    }

    public float getRange() {
        return this.range;
    }

    public float getAP() {
        return this.AP;
    }

}
