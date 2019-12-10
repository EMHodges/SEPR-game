package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bomb extends Sprite {
    private Vector2 position;
    private Vector2 targetPosition;
    private float velocity;
    private FireTruck truck;
    private float damage;

    public Bomb(float x, float y, FireTruck truck, float damage, float velocity) {
        this.position = new Vector2(x,y);
        this.velocity = velocity;
        this.targetPosition = getMiddleOfTile(truck.getPosition());
        this.truck = truck;
        this.damage = damage;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public boolean checkHit() {
        return (getMiddleOfTile(position).equals(getMiddleOfTile(truck.getPosition())));
    }

    public void update(float delta) {
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

    private Vector2 getMiddleOfTile(Vector2 pos) {
        return new Vector2((int) pos.x + 0.5f, (int) pos.y + 0.5f);
    }

    public void boom() {
        this.truck.fortressDamage(this.damage);
    }

    public Vector2 getTargetPos(){
        return this.targetPosition;
    }

}
