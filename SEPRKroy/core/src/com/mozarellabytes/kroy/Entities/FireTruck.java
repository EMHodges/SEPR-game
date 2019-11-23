package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;

public class FireTruck extends Sprite {

    private GameScreen gameScreen;

    private int AP, range, type;
    private float HP, reserve;
    private float maxHP, maxReserve;
    private double speed;
    private float x, y;
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;
    public Queue<Vector2> path;
    public Queue<Vector2> trailPath;
    private boolean moving;

    private boolean seeStats;

    private Texture trailImage;

    private Vector2 lastCoordinate;

    public FireTruck(GameScreen gameScreen, float x, float y, double speed, String colour) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_" + colour + ".png")));

        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.HP = 0;
        this.reserve = 30;
        this.maxHP = 80;
        this.maxReserve = 60;

        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000_" + colour + ".png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_" + colour + ".png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000_" + colour + ".png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000_" + colour + ".png"));
        path = new Queue<Vector2>();
        trailPath = new Queue<Vector2>();
        moving = false;

        trailImage = new Texture(Gdx.files.internal("sprites/firetruck/" + colour + "_trail.png"));
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
            followPath();
        }
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public Queue<Vector2> getPath() {
        return this.trailPath;
    }

    public Texture getTrailImage() {
        return this.trailImage;
    }

    public boolean seeStats() {
        return this.seeStats;
    }

    public void setSeeStats(boolean t) {
        this.seeStats = t;
    }

    public void addTileToPath(Vector2 coordinate) {
        if (this.path.size > 0) {
            Vector2 previous = this.path.last();
            int interpolation = (int) (5/speed);
            for (int i=0; i<interpolation; i++) {
                this.path.addLast(new Vector2((((previous.x - coordinate.x)*-1)/interpolation)*i + previous.x, (((previous.y - coordinate.y)*-1)/interpolation)*i + previous.y));
            }
        }
        this.trailPath.addLast(new Vector2(((int) coordinate.x), ((int) coordinate.y)));
        this.path.addLast(new Vector2(((int) coordinate.x), ((int) coordinate.y)));
    }

    public void resetTilePath() {
        this.path.clear();
    }

    public boolean isValidMove(Vector2 coordinate) {
        if (gameScreen.isRoad((Math.round(coordinate.x)), (Math.round(coordinate.y)))) {
            if (this.path.isEmpty()) {
                return true;
            } else {
                if (!this.path.last().equals(coordinate)) {
                    if ((int)Math.abs(this.path.last().x - coordinate.x) + (int)Math.abs(this.path.last().y - coordinate.y) <= 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public void followPath() {
        if (this.path.size > 0) {
            Vector2 nextTile = path.first();
            this.x = nextTile.x;
            this.y = nextTile.y;

            if (this.trailPath.size != 0) {
                if (((int) this.x) == this.trailPath.first().x && ((int) this.y) == this.trailPath.first().y) {
                    this.trailPath.removeFirst();
                }
            }
            changeSprite(nextTile);

            lastCoordinate = nextTile;
            path.removeFirst();
        } else {
            moving = false;
            this.trailPath.clear();
        }
    }
    private void changeSprite(Vector2 nextTile) {
        if (lastCoordinate != null) {
            if (nextTile.x > lastCoordinate.x) { setTexture(lookRight); }
            else if (nextTile.x < lastCoordinate.x) { setTexture(lookLeft); }
            else if (nextTile.y > lastCoordinate.y) { setTexture(lookUp);  }
            else if (nextTile.y < lastCoordinate.y) { setTexture(lookDown);
            }
        }
    }

    protected void attack() {

    }

    public void repair() {
        this.HP += 0.04f;
    }

    public void refill() {
        this.reserve += 0.06f;
    }

    public float getHP(){
        return this.HP;
    }

    public float getReserve(){
        return this.reserve;
    }

    public float getMaxHP(){
        return this.maxHP;
    }

    public float getMaxReserve(){
        return this.maxReserve;
    }
}



