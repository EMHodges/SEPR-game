package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Bomb extends Sprite {

    private final FireTruck truck;

    private final Vector2 truckPosition;
    private final Vector2 startPosition;
    private Vector2 currentPosition;
    private final Vector2 targetPosition;

    private final float damage;

    public Bomb(Fortress fortress, FireTruck truck) {

        this.truck = truck;
        Vector2 truckPosition = truck.getPosition();
        this.truckPosition = new Vector2(getMiddleOfTile(truckPosition));
        this.startPosition = new Vector2(fortress.getPosition());
        this.currentPosition = this.startPosition;

        // Note: Target position is different to the truck position
        // Target position is the tile where the bomb "lands" which is not always the tile where the Truck is
        Vector2 targetPosition = generateBombTarget();
        this.targetPosition = getMiddleOfTile(targetPosition);

        this.damage = fortress.getFortressType().getAP();
    }

    public Vector2 getPosition() { return this.currentPosition; }

    public boolean checkHit() {
        return targetPosition.equals(truckPosition) && getMiddleOfTile(this.currentPosition).equals(truckPosition);
    }

    public void newUpdatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.03f, Interpolation.pow5Out);
    }

    private Vector2 getMiddleOfTile(Vector2 pos) { return new Vector2((int) pos.x + 0.5f, (int) pos.y + 0.5f); }

    public void damageTruck() { this.truck.fortressDamage(this.damage); }

    public Vector2 getTargetPos() {
        return this.targetPosition;
    }

    public Vector2 generateBombTarget() {
        // Generates a Vector2 of a tile near to (and sometimes including) the truck's position
        float xCoord = (int)(Math.random() * (((truckPosition.x + 1) - (truckPosition.x - 1) + 1)));
        float yCoord = (int)(Math.random() * (((truckPosition.y + 1) - (truckPosition.y - 1) + 1)));
        Vector2 positionNearTruck = new Vector2(truckPosition.x - 1 + xCoord, truckPosition.y - 1 + yCoord);
        // Randomly selects whether the bombTarget will be the truck or a position near to the truck
        return new Random().nextBoolean() ? truckPosition : positionNearTruck;
    }

    public void drawBomb(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.setColor(Color.RED);
        shapeMapRenderer.circle(this.currentPosition.x, this.currentPosition.y, 0.2f, 40);
        shapeMapRenderer.setColor(Color.WHITE);
    }
}
