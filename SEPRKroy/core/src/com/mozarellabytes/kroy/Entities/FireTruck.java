package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;
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
    private long timeMoved;

    private GameScreen gameScreen;

    private BodyDef bodyDef;
    private Body b2body;

    public FireTruck(GameScreen gameScreen) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png")));
        this.gameScreen = gameScreen;
        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000.png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000.png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000.png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000.png"));
        path = new Queue<Vector3>();
        location = new Vector3(0, 0, 0);
        moving = false;
        speed = 2;

        x = 9;
        y = 3;

        defineFireTruck();
    }

    private void defineFireTruck() {
        bodyDef = new BodyDef();
        bodyDef.position.set(10, 10);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0f;
        b2body = this.gameScreen.world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
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

    public void mouseMove() {
        if (this.moving) {
            if (System.currentTimeMillis() - this.timeMoved > 100) {
                this.timeMoved = System.currentTimeMillis();
                followPath();
            }
        }
    }

    public int getCellX() {
        return this.x;
    }

    public int getCellY() {
        return this.y;
    }

    public Queue<Vector3> getPath() {
        return this.path;
    }

    public void addTileToPath(Vector3 coordinate) {
        this.path.addLast(new Vector3(((int) coordinate.x), ((int) coordinate.y), 0));
    }

    public void resetTilePath() {
        this.path.clear();
        this.timeMoved = System.currentTimeMillis();
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public void followPath() {
        if (this.path.size > 0) {
            this.x = (int) path.first().x;
            this.y = (int) path.first().y;
            path.removeFirst();
        } else {
            moving = false;
        }
    }

    protected void attack() {

    }

}
