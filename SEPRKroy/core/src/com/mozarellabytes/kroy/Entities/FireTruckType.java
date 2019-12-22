package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum FireTruckType {

    Speed (100, 100, 2, "Red", "Speed Truck", 5, 0.08f),
//    Tank (100,250,1, "Green", "Tank Truck", 5, 0.08f),
    Ocean(250,100,1, "Blue", "Ocean Truck", 8, 0.08f);

    private float maxReserve;
    private float maxHP;
    private float speed;
    private String trailColour;
    private String name;
    private float range;
    private float AP;

    private Texture trailImage;
    private Texture trailImageEnd;

    FireTruckType(int maxReserve, int maxHP, int speed, String trailColour, String name, float range, float AP) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.trailColour = trailColour;
        this.name = name;
        this.range = range;
        this.AP = AP;

        this.trailImage = new Texture(Gdx.files.internal("sprites/firetruck/" + this.trailColour + "_trail.png"));
        this.trailImageEnd = new Texture(Gdx.files.internal("sprites/firetruck/" + this.trailColour + "_trail_end.png"));
    }

    public float getMaxReserve(){
        return this.maxReserve;
    }

    public float getMaxHP(){
        return this.maxHP;
    }

    public float getSpeed(){
        return this.speed;
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

    public Texture getTrailImage(){ return this.trailImage; }

    public Texture getTrailImageEnd() { return this.trailImageEnd; }

    public String getTrailColour() {
        return this.trailColour;
    }

}
