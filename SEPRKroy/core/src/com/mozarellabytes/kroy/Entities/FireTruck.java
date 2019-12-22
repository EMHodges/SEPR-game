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

    private GameScreen gameScreen;

    public FireTruckType type;
    private float HP, reserve;
    private float x, y;
    public Queue<Vector2> path;
    public Queue<Vector2> trailPath;
    private boolean moving;
    private boolean attacking;
    private boolean inCollision;
    private Vector2 lastCoordinate;

    private ArrayList<Particle> spray;

    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;

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

        this.spray = new ArrayList<Particle>();

        this.lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"));
        this.lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"));
        this.lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up.png"));
        this.lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down.png"));

    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public void move() {
        if (this.moving) {
            followPath();
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

    public void resetPath() {
        this.path.clear();
        this.trailPath.clear();
    }


    public Fortress findFortressWithinRange() {
        if (this.attacking) {
            for (Fortress fortress : gameScreen.fortresses) {
                if (new Vector2((float) (this.getPosition().x + 0.5), (float) (this.getPosition().y)).dst(fortress.getPosition()) <= this.type.getRange()) {
                    return fortress;
                }
            }
        }
        return null;
    }


    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public void attack() {
        if (findFortressWithinRange() != null && this.reserve > 0) {
            this.spray.add(new Particle(this, findFortressWithinRange()));
            this.reserve -= Math.min(this.reserve, this.type.getAP());
        }
    }

    public ArrayList<Particle> getSpray() {
        return this.spray;
    }

    public void updateSpray(float delta) {
        if (this.spray != null) {
            for (Particle particle : this.spray) {
                particle.updatePosition(delta);
            }
        }
    }

    public void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }

    public void damage(Particle particle) { particle.getTarget().damage(this.type.getAP()); }

    public void fortressDamage(float HP) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_damage.play();
        }
        this.HP -= HP;
    }


    public void drawPath(Batch batch) {
        if (!this.trailPath.isEmpty()) {
            batch.setColor(Color.RED);
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
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, (float) this.getReserve() / (float) this.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, (float) this.getHP() / (float) this.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
        for (Particle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
    }

    public void drawSprite(Batch batch) {
        batch.draw(this, this.getX(), this.getY(), 1, 1);
    }
}

