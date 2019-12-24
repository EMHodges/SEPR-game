package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum FortressType {

    Default ("Default Fortress", 2000, 5, 100, 10, 4, 6, new Texture(Gdx.files.internal("sprites/fortress/fortress.png"))),
    Walmgate ("Walmgate Bar", 3000, 7, 150, 15, 5, 5, new Texture(Gdx.files.internal("sprites/fortress/fortress_walmgate.png"))),
    Clifford ("Clifford's Tower", 700, 4, 150, 20, 4, 3, new Texture(Gdx.files.internal("sprites/fortress/fortress_clifford.png")));

    private String name;
    private int delay;
    private float range, maxHP, AP;

    private int w, h;
    private Texture texture;

    FortressType(String name, int delay, float range, float maxHP, float AP, int w, int h, Texture texture) {
        this.name = name;
        this.delay = delay;
        this.range = range;
        this.maxHP = maxHP;
        this.AP = AP;
        this.w = w;
        this.h = h;
        this.texture = texture;
    }

    public float getAP() {
        return AP;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getMaxHP() {
        return maxHP;
    }

    public float getRange() {
        return range;
    }

    public int getDelay() {
        return delay;
    }

    public String getName() {
        return name;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
