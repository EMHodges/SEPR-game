package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

import java.util.Stack;

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

    public FireTruck() {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png")));
        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000.png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000.png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000.png"));
        speed = 3;
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
