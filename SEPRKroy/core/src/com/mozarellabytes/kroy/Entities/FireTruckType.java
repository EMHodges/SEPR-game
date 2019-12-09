package com.mozarellabytes.kroy.Entities;

public enum FireTruckType {

    Speed (100, 100, 2, "Red", "Speed Truck"),
    Tank (100,250,1, "Green", "Tank Truck"),
    Ocean(250,100,1, "Blue", "Ocean Truck");

    private int maxReserve;
    private int maxHP;
    private int speed;
    private String colour;
    private String name;

    FireTruckType(int maxReserve, int maxHP, int speed, String colour, String name) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.colour = colour;
        this.name = name;
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

    public String getName() {
        return this.name;
    }

}
