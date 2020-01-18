package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * This class is what a FireTruck uses to attack a
 * Fortress with. It generates a random colour from
 * a list of colours and a random size before heading
 * from the FireTruck towards the Fortress using the
 * interpolation function specified
 */
public class WaterParticle {

    /** Fortress that WaterParticle is firing at */
    private final Fortress target;

    /** Random colour of Rectangle */
    private final Color colour;

    /** Random size of the Rectangle */
    private final float size;

    /** The position where the water particle starts from (the position
     * of the truck)
     */
    private final Vector2 startPosition;

    /** The current position of the water particle */
    private Vector2 currentPosition;

    /** The end position of the water particle (the fortress the truck
     * is attacking)
     */
    private Vector2 targetPosition;

    /**
     * Constructs a WaterParticle with
     * the following parameters
     *
     * @param truck     The FireTruck that the
     *                  WaterParticle came from
     * @param target    The Fortress that the
     *                  WaterParticle is heading
     *                  towards
     */
    public WaterParticle(FireTruck truck, Fortress target) {
        this.target = target;
        Color[] colors = new Color[] {Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
        this.colour = colors[(int)(Math.random() * 4)];
        this.size = (float)(Math.random()/5 + 0.1);
        this.startPosition = new Vector2(truck.getPosition().x + 0.5f, truck.getPosition().y + 0.5f);
        this.currentPosition = startPosition;
        this.targetPosition = target.getPosition();
        createTargetPosition(target);
    }

    /**
     * Creates the random coordinate within the fortress
     *
     * @param fortress the fortress whose target position is being created
     */
    private void createTargetPosition(Fortress fortress) {
        float xCoord = (float)(Math.random()-0.5+fortress.getPosition().x);
        float yCoord = (float)(Math.random()-0.5+fortress.getPosition().y);
        this.targetPosition = new Vector2(xCoord, yCoord);
    }

    /**
     * Updates the position of the WaterParticle
     * using the Interpolation function
     */
    public void updatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.2f, Interpolation.circle);
    }

    /**
     * Checks if the WaterParticle has
     * reached the the Fortress
     *
     * @return  <code>true</code> if WaterParticle hit Fortress
     *          <code>false</code> otherwise
     */
    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.currentPosition.x) &&
                ((int) this.targetPosition.y == (int) this.currentPosition.y));
    }

    public Fortress getTarget() { return this.target; }

    public float getSize() { return this.size; }

    public Color getColour() { return this.colour; }

    public Vector2 getPosition() { return this.currentPosition; }

}

