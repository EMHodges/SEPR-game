package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

public class Fortress {

    /*** Fortress health, destroyed on zero */
    private float HP;

    /*** Position of the Fortress */
    private final Vector2 position;

    /*** Where the Fortress lies on the map */
    private final Rectangle area;

    /*** List of bombs that are active */
    private final ArrayList<Bomb> bombs;

    /*** Gives Fortress certain stats */
    private final FortressType fortressType;

    /**
     * Constructs Fortress at certain position and
     * of a certain type
     *
     * @param x     x coordinate of Fortress (lower left point)
     * @param y     y coordinate of Fortress (lower left point)
     * @param type  Type of Fortress to give certain stats
     */
    public Fortress(float x, float y, FortressType type) {
        this.fortressType = type;
        this.position = new Vector2(x, y);
        this.HP = type.getMaxHP();
        this.bombs = new ArrayList<Bomb>();
        this.area = new Rectangle(this.position.x - (float) this.fortressType.getW()/2, this.position.y - (float) this.fortressType.getH()/2,
                this.fortressType.getW(), this.fortressType.getH());
    }

    /**
     * Checks if the truck's position is within the attack range of the fortress
     *
     * @param targetPos the truck position being checked
     * @return          <code>true</code> if truck within range of fortress
     *                  <code>false</code> otherwise
     */
    public boolean withinRange(Vector2 targetPos) {
        return targetPos.dst(this.position) <= fortressType.getRange();
    }

    /**
     * Generates bombs to attack the FireTruck with
     * @param target        FireTruck being attacked
     * @param randomTarget  whether the bomb hits every time or
     *                      there is a chance it misses
     */
    public void attack(FireTruck target, boolean randomTarget) {
        if (target.getTimeOfLastAttack() + fortressType.getDelay() < System.currentTimeMillis()) {
            this.bombs.add(new Bomb(this, target, randomTarget));
            target.setTimeOfLastAttack(System.currentTimeMillis());
            if (SoundFX.music_enabled) {
                SoundFX.sfx_fortress_attack.play();
            }
        }
    }

    /**
     * Updates the position of all the bombs and checks whether
     * they have hit their target. If they have, it should deal
     * damage to the truck, remove the bomb and shake the screen
     * @return  <code>true</code> if bomb hits a truck
     *          <code>false</code> if bomb does nt hit a true
     */
    public boolean updateBombs() {
        for (int i = 0; i < this.getBombs().size(); i++) {
            Bomb bomb = this.getBombs().get(i);
            bomb.updatePosition();
            if (bomb.checkHit()) {
                bomb.damageTruck();
                this.removeBomb(bomb);
                return true;
            } else if (bomb.hasReachedTargetTile()) {
                this.removeBomb(bomb);
            }
        }
        return false;
    }

    /**
     * Removes Bomb from bomb list. This
     * occurs when the bomb hits or misses
     *
     * @param bomb bomb being removed
     */
    private void removeBomb(Bomb bomb) {
        this.bombs.remove(bomb);
    }

    /**
     * Draws the health bars above the Fortress
     *
     * @param shapeMapRenderer  The renderer to be drawn to
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x - 0.26f, this.getPosition().y + 1.4f, 0.6f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, this.getHP() / this.fortressType.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    /**
     * Draws the Fortress on the map
     *
     * @param mapBatch  the renderer in line with the map
     */
    public void draw(Batch mapBatch) {
        mapBatch.draw(this.getFortressType().getTexture(), this.getArea().x, this.getArea().y, this.getArea().width, this.getArea().height);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getHP() {
        return this.HP;
    }

    public void damage(float HP){
        this.HP -= HP;
    }

    public Rectangle getArea() {
        return this.area;
    }

    public FortressType getFortressType() {
        return this.fortressType;
    }

    public ArrayList<Bomb> getBombs() {
        return this.bombs;
    }

}
