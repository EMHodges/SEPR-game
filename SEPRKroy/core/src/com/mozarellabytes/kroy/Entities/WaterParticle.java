package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class WaterParticle {

    private Fortress target;
    private Color colour;
    private float size;
    private Vector2 startPosition;
    private Vector2 currentPosition;
    private Vector2 targetPosition;

    public WaterParticle(FireTruck truck, Fortress target) {
        this.target = target;
        Color[] colors = new Color[] { Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
        Color randomColor = colors[(int)( Math.random() * 4)];
        this.colour = randomColor;
        this.size = (float)(Math.random()/5 + 0.1);
        this.startPosition = new Vector2(truck.getPosition().x + 0.5f, truck.getPosition().y + 0.5f);
        this.currentPosition = startPosition;
        this.targetPosition = target.getPosition();

        createTargetPosition(target);
    }

    public Vector2 getPosition() {
        return this.currentPosition;
    }

    public void updatePosition(float delta) {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.2f, Interpolation.circle);
    }

    public Color getColour() {
        return this.colour;
    }

    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.currentPosition.x) &&
                ((int) this.targetPosition.y == (int) this.currentPosition.y));
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
