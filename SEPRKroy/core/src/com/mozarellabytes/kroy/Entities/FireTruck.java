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
    private Texture trailImageEnd;

    private Vector2 lastCoordinate;

    public FireTruck(GameScreen gameScreen, float x, float y, TruckType type) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000_" + type.name() + ".png")));

        this.gameScreen = gameScreen;

        switch(type) {
            case Ocean:
                this.maxReserve = 250;
                this.maxHP = 100;
                this.speed = 1;
                break;
            case Speed:
                this.maxReserve = 100;
                this.maxHP = 100;
                this.speed = 2;
                break;
            case Tank:
                this.maxReserve = 100;
                this.maxHP = 250;
                this.speed = 1;
                break;
        }

        this.reserve = this.maxReserve;
        this.HP = this.maxHP;

        this.x = x;
        this.y = y;

        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000_" + type.name() + ".png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_" + type.name() + ".png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000_" + type.name() + ".png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000_" + type.name() + ".png"));
        path = new Queue<Vector2>();
        trailPath = new Queue<Vector2>();
        moving = false;

        trailImage = new Texture(Gdx.files.internal("sprites/firetruck/" + type.name() + "_trail.png"));
        trailImageEnd = new Texture(Gdx.files.internal("sprites/firetruck/" + type.name() + "_trail_end.png"));
    }

    public enum TruckType {
        Speed,
        Tank,
        Ocean
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

    public Texture getTrailImageEnd() {
        return this.trailImageEnd;
    }

    public void addTileToPath(Vector2 coordinate) {
        if (this.path.size > 0) {
            Vector2 previous = this.path.last();
            int interpolation = (int) (20/speed);
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
        if (coordinate.y < 24) {
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

    public void attack() {
        if (this.reserve > 1f) {
            gameScreen.fortress.damage(5f);
            this.reserve -= 5f;
        } else {
            this.reserve = 0;
        }
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

    public void fortressDamage(float HP) {
        this.HP -= HP;
    }

    public boolean inFortresssRange(){
        if (new Vector2((float) (this.getPosition().x + 0.5), (float) (this.getPosition().y)).dst(gameScreen.fortress.getPosition()) <= gameScreen.fortress.getRange()) {
            return true;
        }
        return false;
    }


}



