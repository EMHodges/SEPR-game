package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

public class FireTruck extends Sprite {

    private final GameScreen gameScreen;

    public final FireTruckType type;
    private float HP, reserve;
    private float x, y;
    public final Queue<Vector2> path;
    public final Queue<Vector2> trailPath;
    private boolean moving;
    private boolean attacking;
    private boolean inCollision;
    private Vector2 lastCoordinate;

    private long timeOfLastAttack;
    private ArrayList<WaterParticle> spray;

    private final Texture lookLeft;
    private final Texture lookRight;
    private final Texture lookUp;
    private final Texture lookDown;

    public FireTruck(GameScreen gameScreen, float x, float y, FireTruckType type) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/down.png")));

        this.gameScreen = gameScreen;

        this.type = type;
        this.HP = type.getMaxHP();
        this.reserve = type.getMaxReserve();

        this.x = x;
        this.y = y;

        this.path = new Queue<Vector2>();
        this.trailPath = new Queue<Vector2>();

        this.moving = false;
        this.attacking = false;
        this.inCollision = false;

        this.spray = new ArrayList<WaterParticle>();

        this.lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"));
        this.lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"));
        this.lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up.png"));
        this.lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down.png"));

        this.timeOfLastAttack = System.currentTimeMillis();
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public void move() {
        if (this.moving) {
            followPath();
        }
    }

    public void repair(float HP) {
        this.HP += HP;
    }

    public void refill(float reserve) {
        this.reserve += reserve;
    }

    public float getHP(){
        return this.HP;
    }

    public float getReserve() { return this.reserve; }

    public FireTruckType getType(){ return this.type; }

    public void setCollision(){
        this.inCollision = true;
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

    public boolean isValidMove(Vector2 coordinate) {
        if (coordinate.y < 24) {
            if (gameScreen.isRoad((Math.round(coordinate.x)), (Math.round(coordinate.y)))) {
                if (this.path.isEmpty()) {
                    return (this.getPosition().equals(coordinate));
                } else {
                    if (!this.path.last().equals(coordinate)) {
                        return (int) Math.abs(this.path.last().x - coordinate.x) + (int) Math.abs(this.path.last().y - coordinate.y) <= 1;
                    }
                }
            }
        }
        return false;
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

    public void followPath() {
        if (this.path.size > 0) {
            Vector2 nextTile = path.first();
            this.x = nextTile.x;
            this.y = nextTile.y;

            if (!this.trailPath.isEmpty() && (int) this.x == this.trailPath.first().x && (int) this.y == this.trailPath.first().y) {
                this.trailPath.removeFirst();
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

    public void resetPath() {
        this.path.clear();
        this.trailPath.clear();
    }


    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public void attack(Fortress fortress) {
        if (this.attacking && this.reserve > 0){
            this.spray.add(new WaterParticle(this, fortress));
            this.reserve -= Math.min(this.reserve, this.type.getAP());
        }
    }

    public boolean fortressInRange(Fortress fortress) {
        if (this.getPosition().dst(fortress.getPosition()) <= this.type.getRange()){
            return true;
        }
        return false;
    }


    public ArrayList<WaterParticle> getSpray() {
        return this.spray;
    }

    public void updateSpray(float delta) {
        if (this.spray != null) {
            for (WaterParticle particle : this.spray) {
                particle.updatePosition(delta);
            }
        }
    }

    public void removeParticle(WaterParticle particle) {
        this.spray.remove(particle);
    }

    public void damage(WaterParticle particle) { particle.getTarget().damage(this.type.getAP()); }

    public void fortressDamage(float HP) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_damage.play();
        }
        this.HP -= HP;
    }

    public void drawPath(Batch batch) {
        if (!this.trailPath.isEmpty()) {
            batch.setColor(this.type.getTrailColour());
            for (Vector2 tile : this.trailPath) {
                if (tile.equals(this.trailPath.last())) {
                    batch.draw(this.type.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                }
                batch.draw(this.type.getTrailImage(), tile.x, tile.y, 1, 1);
            }
            batch.setColor(Color.WHITE);
        }
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.6f, 0.8f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, this.getReserve() / this.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, (float) this.getHP() / (float) this.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
        for (WaterParticle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
    }

    public void drawSprite(Batch batch) {
        batch.draw(this, this.getX(), this.getY(), 1, 1);
    }

    public long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }
    public void resetTimeOfLastAttack(){
        this.timeOfLastAttack = System.currentTimeMillis();
    }
}

