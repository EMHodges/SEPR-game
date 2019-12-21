package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bomb extends Sprite {
    private Vector2 position;
    private Vector2 targetPosition;
    private FireTruck truck;
    private float damage;
    private Vector2 originalPosition;

    public Bomb(float x, float y, FireTruck truck, float damage) {
        this.position = new Vector2(x,y);
        this.targetPosition = getMiddleOfTile(truck.getPosition());
        this.truck = truck;
        this.damage = damage;
        this.originalPosition = this.position;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public boolean checkHit() {
        return (getMiddleOfTile(position).equals(getMiddleOfTile(truck.getPosition())));
    }

    public void newUpdatePosition() {
        this.position = this.originalPosition.interpolate(this.targetPosition, 0.03f, Interpolation.pow5Out);
    }

    private Vector2 getMiddleOfTile(Vector2 pos) {
        return new Vector2((int) pos.x + 0.5f, (int) pos.y + 0.5f);
    }

    public void damageTruck() {
        this.truck.fortressDamage(this.damage);
    }

    public Vector2 getTargetPos(){
        return this.targetPosition;
    }

}
