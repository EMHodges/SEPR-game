package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum FireTruckType {

    Speed (100, 100, 2, Color.RED, "Speed Truck", 5, 0.08f),
    Ocean(250,100,1, Color.CYAN, "Ocean Truck", 8, 0.08f);

    private float maxReserve;
    private float maxHP;
    private float speed;
    private Color trailColour;
    private String name;
    private float range;
    private float AP;

    private Texture trailImage;
    private Texture trailImageEnd;

    FireTruckType(int maxReserve, int maxHP, int speed, Color trailColour, String name, float range, float AP) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
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
