package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

public class Fortress {

    private GameScreen gameScreen;
    private int type, HP, AP, range;
    private Vector2 position;
    private Texture texture;

    public Fortress(GameScreen gameScreen, int x, int y, int range) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.range = range;
        this.texture = new Texture(Gdx.files.internal("sprites/station.png"));
    }

    public void checkRange(FireTruck target) {
        if (target.getPosition().dst2(this.position) <= range) {
            target.fortressDamage();
        }
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2 getPosition() {
        return this.position;
    }

}

