package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Particle {

    private Color colour;
    private Vector2 position;
    private Vector2 targetPosition;
    private float velocity = 5f;
    private FireTruck truck;
    private Fortress target;

    public Particle(Vector2 end, FireTruck truck, Fortress target) {
        this.colour = new Color(Color.BLUE);
        this.position = truck.getPosition();
        this.targetPosition = end;
        this.truck = truck;
        this.target = target;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void updatePosition(float delta) {
        if (this.targetPosition.x > this.position.x) {
            position.x += this.velocity*delta;
        } else if (this.targetPosition.x < this.position.x){
            position.x -= this.velocity*delta;
        } if(this.targetPosition.y > this.position.y){
            position.y += this.velocity*delta;
        } else if (this.targetPosition.y < this.position.y){
            position.y -= this.velocity*delta;
        }
    }

    public Color getColour() {
        return this.colour;
    }

    public Vector2 getTargetPosition() {
        return this.targetPosition;
    }

    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.position.x) && ((int) this.targetPosition.x == (int) this.position.x));
    }

    public Fortress getTarget() {
        return this.target;
    }
}
