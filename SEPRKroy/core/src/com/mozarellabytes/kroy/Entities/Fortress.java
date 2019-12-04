package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Random;

public class Fortress {

    private GameScreen gameScreen;
    private int type, HP, AP;
    private float range;
    private Vector2 position;
    private Texture texture;
    Random rand = new Random();

    public Fortress(GameScreen gameScreen, float x, float y, int range) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.range = range;
        this.texture = new Texture(Gdx.files.internal("sprites/fortress.png"));
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
        target.fortressDamage(0.6f);
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

