package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum FireTruckType {

    Speed (100, 2, Color.RED, "Speed Truck", 5, 0.08f),
    Ocean(250, 1, Color.CYAN, "Ocean Truck", 8, 0.16f);

    private final float maxReserve;
    private final float maxHP;
    private final float speed;
    private final Color trailColour;
    private final String name;
    private final float range;
    private final float AP;

    private final Texture trailImage;
    private final Texture trailImageEnd;

    FireTruckType(int maxReserve, int speed, Color trailColour, String name, float range, float AP) {
        this.maxReserve = maxReserve;
        this.maxHP = 100;
        this.speed = speed;
        this.trailColour = trailColour;
        this.name = name;
        this.range = range;
        this.AP = AP;

        this.trailImage = new Texture(Gdx.files.internal("sprites/firetruck/trail.png"));
        this.trailImageEnd = new Texture(Gdx.files.internal("sprites/firetruck/trailEnd.png"));
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

    public Color getTrailColour() {
        return this.trailColour;
    }

}
