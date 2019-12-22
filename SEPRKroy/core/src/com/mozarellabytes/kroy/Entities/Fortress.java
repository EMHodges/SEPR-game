package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;
import java.util.Random;

public class Fortress {

    private GameScreen gameScreen;
    private FortressType type;
    private float HP, AP, maxHP, range;
    private Vector2  position;
    private Texture texture;
    private String name;
    private Rectangle area;
    private Random rand;
    private ArrayList<Bomb> bombs;
    private long lastFire;
    private int delay, h, w;

    public Fortress(GameScreen gameScreen, float x, float y, FortressType type) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);

        this.type = type;
        this.name = type.getName();
        this.range = type.getRange();
        this.maxHP = type.getMaxHP();
        this.HP = maxHP;
        this.AP = type.getAP();
        this.delay = type.getDelay();
        this.texture = type.getTexture();
        this.w = type.getW();
        this.h = type.getH();

        this.rand = new Random();
        this.bombs = new ArrayList<Bomb>();
        this.lastFire = System.currentTimeMillis();
        this.area = new Rectangle(this.position.x-this.w/2, this.position.y-this.h/2, this.w, this.h);
    }

    public void checkRange(FireTruck target) {
        Vector2 targetPos = new Vector2(((float) (target.getPosition().x + 0.5)), (float) (target.getPosition().y + 0.5));
        if (targetPos.dst(this.position) <= range) {
            ArrayList<Vector2> truckTarget = new ArrayList<Vector2>();
            for (int i = -2; i < 2; i++){
                for (int j = -2; j < 2; j++){
                    truckTarget.add(new Vector2((float)targetPos.x + i, (float) (targetPos.y + j)));
                }
            }
            int randomIndex = rand.nextInt(truckTarget.size());
            if (truckTarget.get(randomIndex).equals(targetPos)){
                attack(target);
            }
        }
    }

    private void attack(FireTruck target){
        if (this.lastFire + this.delay < System.currentTimeMillis()) {
            this.lastFire = System.currentTimeMillis();
            this.bombs.add(new Bomb(this.position.x, this.position.y, target, this.AP, 3f));
            if (SoundFX.music_enabled) {
                SoundFX.sfx_fortress_attack.play();
            }
        }
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getRange() {
        return this.range;
    }

    public float getHP() {
        return this.HP;
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public void damage(float HP){
        this.HP -= HP;
    }

    public Rectangle getArea() {
        return this.area;
    }

    public String getName() {
        return this.name;
    }

    public float getAP() {
        return this.AP;
    }

    public ArrayList<Bomb> getBombs() {
        return this.bombs;
    }

    public void removeBomb(Bomb bomb) {
        this.bombs.remove(bomb);
    }

    public void drawRange(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.setColor(Color.WHITE);
        shapeMapRenderer.circle(this.getPosition().x, this.getPosition().y, this.getRange());
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x - 0.26f, this.getPosition().y + 1.4f, 0.6f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, this.getHP() / this.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }
}

