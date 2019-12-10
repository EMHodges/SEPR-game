package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bomb extends Sprite {
    private Texture texture;
    private Rectangle rect;
    private Vector2 position;
    private Vector2 targetPosition;
    private float velocity;
    private FireTruck truck;
    private int lifespan;
    private long timeCreated;
    private float damage;

    public Bomb(float x, float y, FireTruck truck, float damage, int lifespan, float velocity) {
        this.rect = new Rectangle();
        this.position = new Vector2(x,y);
        this.velocity = velocity;
        this.targetPosition = truck.getPosition();
        this.truck = truck;
        this.lifespan = lifespan;
        this.timeCreated = System.currentTimeMillis();
        this.damage = damage;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public boolean checkHit() {
        return (((int) position.x == (int) targetPosition.x) && ((int) position.y == (int) targetPosition.y));
    }

    public void update(float delta) {
        this.targetPosition = this.truck.getPosition();
        if (this.targetPosition.x > this.position.x){
            position.x += this.velocity*delta;
        } else if (this.targetPosition.x < this.position.x){
            position.x -= this.velocity*delta;
        } if(this.targetPosition.y > this.position.y){
            position.y += this.velocity*delta;
        } else if (this.targetPosition.y < this.position.y){
            position.y -= this.velocity*delta;
        }
    }

    public void boom() {
        this.truck.fortressDamage(this.damage);
    }

    public int getLifespan() {
        return this.lifespan;
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

}
