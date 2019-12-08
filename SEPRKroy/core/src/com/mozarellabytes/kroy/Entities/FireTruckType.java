package com.mozarellabytes.kroy.Entities;

public enum FireTruckType {

    Speed (100, 100, 2, "Red"),
    Tank (100,250,1, "Green"),
    Ocean(250,100,1, "Blue");

    private int maxReserve;
    private int maxHP;
    private int speed;
    private String colour;

    FireTruckType(int maxReserve, int maxHP, int speed, String colour) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.colour = colour;
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

    public String getColour(){
        return this.colour;
    }

}
