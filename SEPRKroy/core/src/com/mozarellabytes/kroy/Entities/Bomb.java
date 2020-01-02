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
 * If the bomb hits the truck, it will deal damage.
 */
public class Bomb extends Sprite {

    /**
     * The target FireTruck that the bomb is heading towards
     */
    private final FireTruck target;

    /**
     * The position of the target at bomb creation
     */
    private final Vector2 truckPosition;

    /**
     * The start position of the bomb
     */
    private final Vector2 startPosition;

    /**
     * Current position of the bomb
     */
    private Vector2 currentPosition;

    /**
     * The tile where the bomb "lands"
     */
    private final Vector2 targetPosition;

    /**
     * The amount of damage that is inflicted on hit
     */
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
     * Getter for current position of the bomb
     *
     * @return position of the Bomb
     */
    public Vector2 getPosition() {
        return this.currentPosition;
    }

    /**
     * Checks whether the bomb has hit the target truck
     *
     * @return  <code>true</code> if bomb position is in
     *          same position as target truck's position
     *          <code>false</code> otherwise.
     */
    public boolean checkHit() {
        return targetPosition.equals(truckPosition) && getMiddleOfTile(this.currentPosition).equals(truckPosition);
    }

    /**
     * Updates the current truck position
     * depending on the Interpolation function
     */
    public void newUpdatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.03f, Interpolation.pow5Out);
    }

    /**
     * Helper function to return the middle
     * of the tile when given a position
     *
     * @param position  Exact position in the tile
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
     * @return Position of target
     */
    public Vector2 getTargetPos() {
        return this.targetPosition;
    }

    /**
     * Generates a Vector2 of a tile near to (and
     * sometimes including) the truck's position
     *
     * @return  Target either truck or position
     *          near truck
     */
    private Vector2 generateBombTarget() {
        float xCoord = (int) (Math.random() * (((truckPosition.x + 1) - (truckPosition.x - 1) + 1)));
        float yCoord = (int) (Math.random() * (((truckPosition.y + 1) - (truckPosition.y - 1) + 1)));
        Vector2 positionNearTruck = new Vector2(truckPosition.x - 1 + xCoord, truckPosition.y - 1 + yCoord);
        return new Random().nextBoolean() ? truckPosition : positionNearTruck;
    }

    /**
     * Draws a red circle at the bomb's position
     * 
     * @param shapeMapRenderer The ShapeRenderer that is drawn to
     */
    public void drawBomb(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.setColor(Color.RED);
        shapeMapRenderer.circle(this.currentPosition.x, this.currentPosition.y, 0.2f, 40);
        shapeMapRenderer.setColor(Color.WHITE);
    }
}
