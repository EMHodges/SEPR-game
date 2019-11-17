package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class FireTruck extends Sprite {

    private int HP, AP, range, type, reserve, speed;
    private boolean rightMove;
    private boolean leftMove;
    private boolean downMove;
    private boolean upMove;
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;
    public ArrayList path;

    public FireTruck() {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png")));
        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000.png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000.png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000.png"));
        path = new ArrayList();
        speed = 3;
    }

    public ArrayList getPath() {
        return path;
    }

    public void addTileToPath(Vector3 coordinate) {
        path.add(new Vector3(((int) coordinate.x), ((int) coordinate.y), 0));
    }

    public void resetTilePath() {
        path.clear();
    }

    public void move() {
        if (rightMove) {
            this.translateX(this.speed);
        } else if (leftMove) {
            this.translateX(this.speed*-1);
        } else if (downMove) {
            this.translateY(this.speed*-1);
        } else if (upMove) {
            this.translateY(this.speed);
        }
    }

    public void setMoveLeft(boolean t) {
        if (rightMove && t) {
            rightMove = false;
        } else {
            leftMove = t;
            this.setTexture(lookLeft);
        }
    }

    public void setMoveRight(boolean t) {
        if (leftMove && t) {
            leftMove = false;
        } else {
            rightMove = t;
            this.setTexture(lookRight);
        }
    }

    public void setMoveDown(boolean t) {
        if (upMove && t) {
            upMove = false;
        } else {
            downMove = t;
            this.setTexture(lookDown);
        }
    }

    public void setMoveUp(boolean t) {
        if (downMove && t) {
            downMove = false;
        } else {
            upMove = t;
            this.setTexture(lookUp);
        }
    }

    protected void attack() {

    }


}
