package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Bomb is a class created when a FireTruck is within
 * a Fortress' range, and timer has passed. It will travel
 * towards either the truck or a tile near the truck.
 * If the bomb hits the truck, it will deal damage
 */
public class Bomb extends Sprite {

    /** The target FireTruck that the bomb is heading towards */
    private final FireTruck target;

    /** The position of the target at bomb creation */
    private final Vector2 truckPosition;

    /** The start position of the bomb */
    private final Vector2 startPosition;

    /** Current position of the bomb */
    private Vector2 currentPosition;

    /** The tile where the bomb "lands" */
    private final Vector2 targetPosition;

    /** The amount of damage that is inflicted on hit */
    private final float damage;

    /**
     * Constructs a bomb with the given source and target
     *
     * @param fortress  Fortress that the bomb came from
     * @param target    FireTruck being targeted
     */
    public Bomb(Fortress fortress, FireTruck target) {
        this.target = target;
        this.truckPosition = new Vector2(getMiddleOfTile(target.getPosition()));
        this.startPosition = new Vector2(fortress.getPosition());
        this.currentPosition = this.startPosition;
        // Note: Target position is different to the truck position
        // Target position is the tile where the bomb "lands" which is not always the tile where the Truck is
        this.targetPosition = getMiddleOfTile(generateBombTarget());
        this.damage = fortress.getFortressType().getAP();
    }

    /**
     *
     * @return Position of the Bomb
     */
    public Vector2 getPosition() {
        return this.currentPosition;
    }

    /**
     * Checks whether the bomb has hit the target truck
     *
     * @return  <code>true</code> if the bomb and target truck's position
     *          are in the middle of the same tile
     *          <code>false</code> otherwise.
     */
    public boolean checkHit() {
        return targetPosition.equals(truckPosition) && getMiddleOfTile(this.currentPosition).equals(truckPosition);
    }

    /**
     * Checks if the bomb has reached the calculated target yet
     *
     * @return  <code>true</code> if bomb enters the tile that was targeted
     *          <code>false</code> otherwise
     */
    public boolean hasReachedTargetTile() {
        return (int) this.currentPosition.x == (int) this.targetPosition.x && (int) this.currentPosition.y == (int) this.targetPosition.y;
    }

    /**
     * Updates the current bomb position depending on the Interpolation function.
     *
     */
    public void updatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.03f, Interpolation.pow5Out);
    }

    /**
     * Helper function to return the middle of the tile. It sets the trucks
     * position to the middle of a tile and is needed by checkHit to see
     * if a bomb and a truck are in the middle of the same tile
     *
     * @param position  The whole values of the bottom left hand corner of a tile
     * @return          Position in the middle of the tile
     */
    private Vector2 getMiddleOfTile(Vector2 position) {
        return new Vector2((int) position.x + 0.5f, (int) position.y + 0.5f);
    }

    /**
     * Damages the truck
     */
    public void damageTruck() {
        this.target.fortressDamage(this.damage);
    }


    /**
     * Determines and returns the position where the bomb will 'land.'
     * This is either the truck's current position or a tile near to the truck;
     * this is so the bomb does not always hit the truck.
     *
     * @return  Target either truck or random position near truck
     */
    private Vector2 generateBombTarget() {
        float xCoord = (int) (Math.random() * (((truckPosition.x + 1) - (truckPosition.x - 1) + 1)));
        float yCoord = (int) (Math.random() * (((truckPosition.y + 1) - (truckPosition.y - 1) + 1)));
        Vector2 positionNearTruck = new Vector2(truckPosition.x - 1 + xCoord, truckPosition.y - 1 + yCoord);
        return new Random().nextBoolean() ? truckPosition : positionNearTruck;
    }

    /**
     * Draws the bomb (a red circle) at the bomb's current position
     *
     * @param shapeMapRenderer The ShapeRenderer from GameScreen used to draw
     *                         the bomb
     */
    public void drawBomb(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.setColor(Color.RED);
        shapeMapRenderer.circle(this.currentPosition.x, this.currentPosition.y, 0.2f, 40);
        shapeMapRenderer.setColor(Color.WHITE);
    }
}


