package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FireTruck extends Sprite {

    private int HP, AP, range, type, reserve;

    public FireTruck() {
        super(new Texture(Gdx.files.internal("frame00001.png")));
    }


    protected void move() {


    }

    protected void attack() {

    }


}
