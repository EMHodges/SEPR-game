package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Particle {

    private Color colour;
    private Vector2 position;
    private Vector2 targetPosition;
    private float velocity = 5f;
    private FireTruck truck;
    private Fortress target;
    private Random rand;

    public Particle(Vector2 end, FireTruck truck, Fortress target) {
        Color[] colors = new Color[] { Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
        Color randomColor = colors[(int)( Math.random() * 4)];
        this.colour = randomColor;
        this.position = new Vector2(truck.getPosition().x + 0.5f, truck.getPosition().y + 0.5f);
        this.targetPosition = target.getPosition();
        this.truck = truck;
        this.target = target;
        this.rand = new Random();
        createTargetPosition(target);
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

    public void createTargetPosition(Fortress fortress) {
        float xCoord = (float)(Math.random() * ((fortress.getArea().x + fortress.getArea().width) - fortress.getArea().x) + fortress.getArea().x);
        float yCoord = (float)(Math.random() * ((fortress.getArea().y + fortress.getArea().height) - fortress.getArea().y) + fortress.getArea().y);
        this.targetPosition = new Vector2(xCoord, yCoord);
    }

    public Fortress getTarget() {
        return this.target;
    }

}
