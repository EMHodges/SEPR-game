package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Utilities.Constants;

import java.util.ArrayList;

public class FireTruck extends Sprite {

    private int HP, AP, range, type, reserve, speed;
    private int x, y;
    private boolean rightMove;
    private boolean leftMove;
    private boolean downMove;
    private boolean upMove;
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;
    private Vector3 location;
    public Queue<Vector3> path;
    private boolean moving;

    public FireTruck() {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png")));
        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000.png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000.png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000.png"));
        path = new Queue<Vector3>();
        location = new Vector3(0, 0, 0);
        moving = false;
        speed = 3;

        x = 9;
        y = 3;
    }

    public void arrowMove() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            x -= 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            x += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            y -= 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            y += 1;
        }
    }

    public int getCellX() {
        return this.x;
    }

    public int getCellY() {
        return this.y;
    }

    public Queue<Vector3> getPath() {
        return path;
    }

    public void addTileToPath(Vector3 coordinate) {
        path.addLast(new Vector3(((int) coordinate.x), ((int) coordinate.y), 0));
    }

    public void resetTilePath() {
        path.clear();
    }

    public void setMove(boolean t) {
        moving = t;
    }

    public void followPath() {
        Gdx.app.log("Path", path.toString());
        if (path.size > 0) {
            if (path.first().x > location.x) {
                this.translateX(48f/2);
            } else if (path.first().x < location.x) {
                this.translateX(-48f/2);
            }
            if (path.first().y > location.y) {
                this.translateY(48f/2);
            } else if (path.first().y < location.y) {
                this.translateY(-48f/2);
            }
            location.x = path.first().x;
            location.y = path.first().y;
            path.removeFirst();
        } else {
            moving = false;
        }
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

        if (moving) {
            followPath();
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
