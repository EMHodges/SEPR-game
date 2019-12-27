package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;
import java.util.Random;

public class Fortress {

    private float HP;
    private Vector2  position;
    private Rectangle area;
    private ArrayList<Bomb> bombs;
    private long lastFire;
    private FortressType fortressType;
    private int delay;

    public Fortress(float x, float y, FortressType type) {
        this.fortressType = type;
        this.position = new Vector2(x, y);
        this.HP = type.getMaxHP();
        this.bombs = new ArrayList<Bomb>();
        this.lastFire = System.currentTimeMillis();
        this.area = new Rectangle(this.position.x-this.fortressType.getW()/2, this.position.y-this.fortressType.getH()/2,
                this.fortressType.getW(), this.fortressType.getH());
        this.delay = type.getDelay();
    }


    private boolean IsTruckInRange(FireTruck target) {
        Vector2 targetPos = target.getPosition();
        if (targetPos.dst(this.position) <= fortressType.getRange()) {
            return true;
            }
        return false;
        }



    public void attack(FireTruck target) {
        if (IsTruckInRange(target)) {
            if (this.lastFire + this.delay < System.currentTimeMillis()) {
                Gdx.app.log("getBombTarget", String.valueOf(target));
                this.lastFire = System.currentTimeMillis();
                Vector2 bombtarget = getBombTarget(target);
                this.bombs.add(new Bomb(this, target, bombtarget));
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_fortress_attack.play();
                }
            }
        }
    }

    public Vector2 getBombTarget(FireTruck target){

        float xCoord = (int)(Math.random() * (((target.getPosition().x+1) - (target.getPosition().x - 1) + 1)));
        float yCoord = (int)(Math.random() * (((target.getPosition().y+1) - (target.getPosition().y - 1) + 1)));
        Vector2 guessCoord = new Vector2(target.getX() - 1 + xCoord, target.getY() - 1 + yCoord);
        return new Random().nextBoolean() ? guessCoord : target.getPosition();
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

    public ArrayList<Bomb> getBombs() {
        return this.bombs;
    }

    public void removeBomb(Bomb bomb) {
        this.bombs.remove(bomb);
    }

    public void drawRange(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.setColor(Color.WHITE);
        shapeMapRenderer.circle(this.getPosition().x, this.getPosition().y, this.fortressType.getRange());
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x - 0.26f, this.getPosition().y + 1.4f, 0.6f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, this.getHP() / this.fortressType.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    public FortressType getFortressType() {
        return this.fortressType;
    }

    public void draw(Batch mapBatch) {
        mapBatch.draw(this.getFortressType().getTexture(), this.getArea().x, this.getArea().y, this.getArea().width, this.getArea().height);
    }
}

