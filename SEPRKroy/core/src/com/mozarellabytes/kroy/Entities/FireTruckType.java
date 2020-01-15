package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * FireTruckType is an enum defining the trucks that can be present in the game.
 * Each truck type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous trucks
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 */

public enum FireTruckType {

    /** The preset values for the different truck types includes the type's:
     * maximum reserve, speed, trailColour, name, attack range, attack points
     */
    Speed(100, 2, Color.RED, "Speed Truck", 5, 0.08f),
    Ocean(250, 1, Color.CYAN, "Ocean Truck", 8, 0.16f);

    /** The maximum amount of water this type of truck can have,
     * also the value of the truck's reserve when it is spawned */
    private final float maxReserve;

    /** The maximum amount of health points this type of truck can have,
     * also the value of the truck's health points when it is spawned */
    private final float maxHP;

    /** The speed at which this type of truck moves around the map */
    private final float speed;

    /** The colour of the trail that is visible once a truck's path has
     * been drawn */
    private final Color trailColour;

    /** The name of this type of truck */
    private final String name;

    /** The attack range of this type of truck */
    private final float range;

    /** Attack points - the damage this truck can inflict */
    private final float AP;

    /** The tile sprite that is drawn to GameScreen once a truck's
     * path has been drawn */
    private final Texture trailImage;

    /** The tile sprite that marks the end of the truck's path so the
     * user knows which tile they can continue drawing the truck's
     * path from */
    private final Texture trailImageEnd;

    /**
     * Constructs the FireTruckType
     *
     * @param maxReserve The maximum reserve for this type of truck;
     * @param speed The speed of this type of truck
     * @param trailColour The colour of the truck's path when it has been drawn
     * @param name The name for this type of truck
     * @param range The attack range for this type of truck in tiles
     * @param AP the attack points for this type of truck
     *
     */
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

    public float getMaxReserve(){ return this.maxReserve; }

    public float getMaxHP(){ return this.maxHP; }

    public float getSpeed(){ return this.speed; }

    public Color getTrailColour() { return this.trailColour; }

    public String getName() { return this.name; }

    public float getRange() { return this.range; }

    public float getAP() { return this.AP; }

    public Texture getTrailImage(){ return this.trailImage; }

    public Texture getTrailImageEnd() { return this.trailImageEnd; }

}
