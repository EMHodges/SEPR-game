package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class Particle {

    private Color colour;
    private Vector2 position;
    private Vector2 targetPosition;
    private Vector2 originalPosition;
    private Fortress target;
    private float size;

    public Particle(FireTruck truck, Fortress target) {
        Color[] colors = new Color[] { Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
        Color randomColor = colors[(int)( Math.random() * 4)];
        this.colour = randomColor;
        this.position = new Vector2(truck.getPosition().x + 0.5f, truck.getPosition().y + 0.5f);
        this.originalPosition = this.position;
        this.targetPosition = target.getPosition();
        this.target = target;
        this.size = (float) (Math.random()/5 + 0.1);
        createTargetPosition(target);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void updatePosition(float delta) {
        this.position = this.originalPosition.interpolate(this.targetPosition, 0.2f, Interpolation.circle);
    }

    public Color getColour() {
        return this.colour;
    }

    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.position.x) && ((int) this.targetPosition.y == (int) this.position.y));
    }

    public void createTargetPosition(Fortress fortress) {
        float xCoord = (float)(Math.random() - 0.5 + fortress.getPosition().x);
        float yCoord = (float)(Math.random() - 0.5 + fortress.getPosition().y);
        this.targetPosition = new Vector2(xCoord, yCoord);
    }

    public Fortress getTarget() {
        return this.target;
    }

    public float getSize() {
        return this.size;
    }

}
