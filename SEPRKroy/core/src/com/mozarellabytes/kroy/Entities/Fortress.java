package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

public class Fortress {

    private GameScreen gameScreen;
    private int type, HP, AP;
    private float range;
    private Vector2 position;
    private Texture texture;

    public Fortress(GameScreen gameScreen, float x, float y, int range) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.range = range;
        this.texture = new Texture(Gdx.files.internal("sprites/fortress.png"));
    }

    public void checkRange(FireTruck target) {
        if (new Vector2(((float) (target.getPosition().x + 0.5)), (float) (target.getPosition().y + 0.5)).dst(this.position) <= range) {
            target.fortressDamage();
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

}

