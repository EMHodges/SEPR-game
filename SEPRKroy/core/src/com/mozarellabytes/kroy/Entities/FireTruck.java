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

/**
 * FireTruck is an entity that the player controls. It navigates the map on the
 * roads defined in the Tiled Map by following a path that the user draws.
 *
 * Having 'A' held when within range of a  Fortress will deal damage to it.
 */
public class FireTruck extends Sprite {

    /** Enables access to functions in GameScreen */
    private final GameScreen gameScreen;

    /** Defines set of pre-defined attributes */
    public final FireTruckType type;

    /** Health points */
    private float HP;

    /** Water Reserve */
    private float reserve;

    /** Position of FireTruck in tiles */
    private Vector2 position;

    /** Actual path the truck follows; the fewer item in
     * the path the slower the truck will go */
    public final Queue<Vector2> path;

    /** The visual path that users can see when drawing
     * a firetruck's path */
    public final Queue<Vector2> trailPath;

    /** If the truck is currently moving, determines whether the
     * truck's position should be updated
     *
     * <code>true</code> once the player has drawn a
     * path and has let go of the mouse click
     * <code>false</code> once the truck has got to
     * the end of the path */
    private boolean moving;

    /** If the truck is attacking a Fortress
     *
     * <code>true</code> 'A' key is pressed
     * <code>false</code> 'A' key is not pressed */
    private boolean attacking;

    /** Whether the truck has an unresolved collision
     * with another truck */
    private boolean inCollision;

    /** Used to check if the truck's image should be
     * changed to match the direction it is facing */
    private Vector2 previousTile;

    /** Time since fortress has attacked the truck */
    private long timeOfLastAttack;

    /** List of particles that the truck uses to attack
     * a Fortress */
    private ArrayList<WaterParticle> spray;

    /** Texture for each direction the
     * truck is facing */
    private final Texture lookLeft;
    private final Texture lookRight;
    private final Texture lookUp;
    private final Texture lookDown;

    /**
     * Constructs a new FireTruck at a position and of a certain type
     * which have been passed in
     *
     * @param gameScreen    used to access functions in GameScreen
     * @param position      initial location of the truck
     * @param type          used to have predefined attributes
     */
    public FireTruck(GameScreen gameScreen, Vector2 position, FireTruckType type) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/down.png")));

        this.gameScreen = gameScreen;
        this.type = type;
        this.HP = type.getMaxHP();
        this.reserve = type.getMaxReserve();
        this.position = position;
        this.path = new Queue<Vector2>();
        this.trailPath = new Queue<Vector2>();
        this.moving = false;
        this.attacking = false;
        this.inCollision = false;
        this.spray = new ArrayList<WaterParticle>();
        this.timeOfLastAttack = System.currentTimeMillis();
        this.lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"));
        this.lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"));
        this.lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up.png"));
        this.lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down.png"));
    }

    /**
     * Called every tick and updates the paths to simulate the truck moving along the
     * path
     */
    public void move() {
        if (moving) {
            if (this.path.size > 0) {
                Vector2 nextTile = path.first();
                this.position = nextTile;

                if (!this.trailPath.isEmpty() && (int) this.position.x == this.trailPath.first().x && (int) this.position.y == this.trailPath.first().y) {
                    this.trailPath.removeFirst();
                }
                if (!this.inCollision) {
                    changeSprite(nextTile);
                }
                previousTile = nextTile;
                path.removeFirst();
            } else {
                moving = false;
            }
            if (this.path.isEmpty() && inCollision) {
                inCollision = false;
            }
        }
    }

    /**
     * Increases Health Points of the truck
     *
     * @param HP    increased by this value
     */
    public void repair(float HP) {
        this.HP += HP;
    }

    /**
     * Increases the Reserve of the truck
     *
     * @param reserve increased by this value
     */
    public void refill(float reserve) {
        this.reserve += reserve;
    }

    /**
     * Called when the player drags mouse on GameScreen Coordinate is checked to see
     * whether it is a valid tile to draw to, then adds it to the paths
     *
     * @param coordinate    Position on the screen that the user's mouse is being
     *                      dragged over
     */
    public void addTileToPath(Vector2 coordinate) {
        if (isValidDraw(coordinate)) {
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
    }

    /**
     * Used when drawing the path to check whether the next tile to be added to the path is
     * valid
     *
     * @param coordinate    Position on the screen that the user's mouse is being dragged over
     * @return              <code>true</code> if the coordinate is a valid tile to be added to
     *                      the paths
     *                      <code>false</code> otherwise
     */
    private boolean isValidDraw(Vector2 coordinate) {
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

    /**
     * Changes the direction of the truck depending on the previous tile and the next tile
     *
     * @param nextTile  first tile in the queue (next to be followed)
     */
    private void changeSprite(Vector2 nextTile) {
        if (previousTile != null) {
            if (nextTile.x > previousTile.x) {
                setTexture(lookRight);
            } else if (nextTile.x < previousTile.x) {
                setTexture(lookLeft);
            } else if (nextTile.y > previousTile.y) {
                setTexture(lookUp);
            } else if (nextTile.y < previousTile.y) {
                setTexture(lookDown);
            }
        }
    }

    /**
     * Clears the two paths
     */
    public void resetPath() {
        this.path.clear();
        this.trailPath.clear();
    }

    /**
     * Deals damage to Fortress by generating a WaterParticle and adding
     * it to the spray
     *
     * @param fortress Fortress being attacked
     */
    public void attack(Fortress fortress) {
        if (this.attacking && this.reserve > 0) {
            this.spray.add(new WaterParticle(this, fortress));
            this.reserve -= Math.min(this.reserve, this.type.getAP());
        }
    }

    /**
     * Called every tick to check if a Fortress is within the range of
     *  the truck
     *
     * @param fortress  Fortress' position being checked
     * @return          <code>true</code> if Fortress is within range
     *                  <code>false </code> otherwise
     */
    public boolean fortressInRange(Vector2 fortress) {
        return this.getVisualPosition().dst(fortress) <= this.type.getRange();
    }

    /**
     * Updates the position of each WaterParticle
     */
    public void updateSpray() {
        if (this.spray != null) {
            for (WaterParticle particle : this.spray) {
                particle.updatePosition();
            }
        }
    }

    /**
     * Remove the WaterParticle from the spray when it hits the Fortress
     *
     * @param particle
     */
    public void removeParticle(WaterParticle particle) {
        this.spray.remove(particle);
    }

    /**
     * Damages the Fortress depending on the truck's AP
     *
     * @param particle
     */
    public void damage(WaterParticle particle) {
        particle.getTarget().damage(this.type.getAP());
    }

    /**
     * Damage inflicted on truck by a fortress, called when a bomb hits a truck it plays
     * a sound and decreases the fire trucks HP by the amount of the fortresses AP
     *
     * @param HP    amount of HP being taken away dependant
     *              on the AP of the attacking Fortress
     */
    public void fortressDamage(float HP) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_damage.play();
        }
        this.HP -= HP;
    }

    /**
     *  Draws the visual, colourful path that the truck will follow
     *
     * @param mapBatch  Batch that the path is being drawn to (map dependant)
     */
    public void drawPath(Batch mapBatch) {
        if (!this.trailPath.isEmpty()) {
            mapBatch.setColor(this.type.getTrailColour());
            for (Vector2 tile : this.trailPath) {
                if (tile.equals(this.trailPath.last())) {
                    mapBatch.draw(this.type.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                }
                mapBatch.draw(this.type.getTrailImage(), tile.x, tile.y, 1, 1);
            }
            mapBatch.setColor(Color.WHITE);
        }
    }

    /**
     * Draws the mini health/reserve indicators relative to the truck
     *
     * @param shapeMapRenderer  Renderer that the stats are being drawn to (map  dependant)
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.6f, 0.8f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, this.getReserve() / this.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, this.getHP() / this.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
        for (WaterParticle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
    }

    /**
     * Draws the FireTruck sprite
     *
     * @param mapBatch  Batch that the truck is being
     *                  drawn to (map dependant)
     */
    public void drawSprite(Batch mapBatch) {
        mapBatch.draw(this, this.position.x, this.position.y, 1, 1);
    }

    /**
     * Helper method that returns where the truck is visually to the player. This is used when
     * checking the range when attacking the Fortress and getting attacked by the Fortress
     *
     * @return a vector where the truck is visually
     */
    public Vector2 getVisualPosition() {
        return new Vector2(this.position.x + 0.5f, this.position.y + 0.5f);
    }

    /**
     * Sets the timeOfLastAttack to the current system time as a Unix Timestamp
     */
    public void resetTimeOfLastAttack() {
        this.timeOfLastAttack = System.currentTimeMillis();
    }

    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }

    public float getHP() {
        return this.HP;
    }

    public float getReserve() {
        return this.reserve;
    }

    public FireTruckType getType() {
        return this.type;
    }

    public void setCollision() {
        this.inCollision = true;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Queue<Vector2> getPath() {
        return this.trailPath;
    }

    public ArrayList<WaterParticle> getSpray() {
        return this.spray;
    }

    public boolean getMoving() {
        return this.moving;
    }
}

