package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FireTruck extends Sprite {

    private int HP, AP, range, type, reserve;

    private Sprite sprite;
    private Texture texture;


    public FireTruck() {

        texture = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000.png"));
        sprite = new Sprite(texture);
    }

    protected void move() {


    }

    protected void attack() {

    }


}
