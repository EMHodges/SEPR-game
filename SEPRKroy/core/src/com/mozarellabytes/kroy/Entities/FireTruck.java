package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

public class FireTruck extends Sprite {

    private GameScreen gameScreen;

    public FireTruckType type;
    private String colour;
    private float HP, reserve, maxHP, maxReserve, range, AP;
    private double speed;
    private float x, y;
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;
    public Queue<Vector2> path;
    public Queue<Vector2> trailPath;
    private boolean moving;
    private boolean attacking;
    private boolean inCollision;

    private String name;

    private ArrayList<Particle> spray;

    private Texture trailImage;
    private Texture trailImageEnd;

    private Vector2 lastCoordinate;

    public FireTruck(GameScreen gameScreen, float x, float y, FireTruckType type) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/down.png")));

        this.gameScreen = gameScreen;

        this.type = type;
        this.speed = type.getSpeed();
        this.reserve = type.getMaxReserve();
        this.maxReserve = type.getMaxReserve();
        this.HP = type.getMaxHP();
        this.maxHP = type.getMaxHP();
        this.colour = type.getColour();
        this.name = type.getName();
        this.AP = type.getAP();
        this.range = type.getRange();
        this.inCollision = false;

        this.x = x;
        this.y = y;

        this.attacking = false;

        this.spray = new ArrayList<Particle>();

        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up.png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down.png"));
        path = new Queue<Vector2>();
        trailPath = new Queue<Vector2>();
        moving = false;

        trailImage = new Texture(Gdx.files.internal("sprites/firetruck/" + colour + "_trail.png"));
        trailImageEnd = new Texture(Gdx.files.internal("sprites/firetruck/" + colour + "_trail_end.png"));
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
            int interpolation = (int) (20/type.getSpeed());
            for (int i=0; i<interpolation; i++) {
                this.path.addLast(new Vector2((((previous.x - coordinate.x)*-1)/interpolation)*i + previous.x, (((previous.y - coordinate.y)*-1)/interpolation)*i + previous.y));
            }
        }
        this.trailPath.addLast(new Vector2(((int) coordinate.x), ((int) coordinate.y)));
        this.path.addLast(new Vector2(((int) coordinate.x), ((int) coordinate.y)));
    }

    public void resetPath() {
        this.path.clear();
        this.trailPath.clear();
    }

    public boolean isValidMove(Vector2 coordinate) {
        if (coordinate.y < 24) {
            if (gameScreen.isRoad((Math.round(coordinate.x)), (Math.round(coordinate.y)))) {
                if (this.path.isEmpty()) {
                    if ((this.getPosition().equals(coordinate))) {
                        return true;
                    }
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
            if (!this.inCollision) {
                changeSprite(nextTile);
            }
            lastCoordinate = nextTile;
            path.removeFirst();
        } else {
            moving = false;
        }
        if (this.path.isEmpty() && inCollision){
            inCollision = false;
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
        if (findFortressWithinRange() != null && this.reserve > 0) {
            this.spray.add(new Particle(this, findFortressWithinRange()));
            this.reserve -= Math.min(this.reserve, this.AP);
        }
    }

    public void updateSpray(float delta) {
        if (this.spray != null) {
            for (Particle particle : this.spray) {
                particle.updatePosition(delta);
            }
        }
    }

    public void damage(Particle particle) {
        particle.getTarget().damage(this.AP);
    }

    public Fortress findFortressWithinRange() {
        if (this.attacking) {
            for (Fortress fortress : gameScreen.fortresses) {
                if (new Vector2((float) (this.getPosition().x + 0.5), (float) (this.getPosition().y)).dst(fortress.getPosition()) <= this.range) {
                    return fortress;
                }
            }
        }
        return null;
    }

    public void setCollision(){
        this.inCollision = true;
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

    public float getReserve() { return this.reserve; }

    public void fortressDamage(float HP) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_damage.play();
        }
        this.HP -= HP;
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public float getMaxReserve() {
        return this.maxReserve;
    }

    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public String getName() {
        return this.name;
    }

    public double getSpeed() {
        return this.speed;
    }

    public float getAP() {
        return this.AP;
    }

    public float getRange() {
        return this.range;
    }

    public ArrayList<Particle> getSpray() {
        return this.spray;
    }

    public void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }
}

