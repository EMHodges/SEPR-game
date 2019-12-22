package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;
import java.util.Random;

public class Fortress {

    private float HP;
    private Vector2  position;
    private Rectangle area;
    private Random rand;
    private ArrayList<Bomb> bombs;
    private long lastFire;
    private FortressType fortressType;

    public Fortress(float x, float y, FortressType type) {
        this.fortressType = type;
        this.position = new Vector2(x, y);
        this.HP = type.getMaxHP();
        this.rand = new Random();
        this.bombs = new ArrayList<Bomb>();
        this.lastFire = System.currentTimeMillis();
        this.area = new Rectangle(this.position.x-this.fortressType.getW()/2, this.position.y-this.fortressType.getH()/2,
                this.fortressType.getW(), this.fortressType.getH());
    }

    private boolean IsTruckInRange(FireTruck target) {
        Vector2 targetPos = new Vector2(((float) (target.getPosition().x + 0.5)), (float) (target.getPosition().y + 0.5));
        if (targetPos.dst(this.position) <= fortressType.getRange()) {
            ArrayList<Vector2> truckTarget = new ArrayList<Vector2>();
            for (int i = -2; i < 2; i++){
                for (int j = -2; j < 2; j++){
                    truckTarget.add(new Vector2(targetPos.x + i, targetPos.y + j));
                }
            }
            int randomIndex = rand.nextInt(truckTarget.size());
            if (truckTarget.get(randomIndex).equals(targetPos)){
                return true;
            }
        }
        return false;
    }

    public void attack(FireTruck target) {
        if (IsTruckInRange(target)) {
            if (this.lastFire + this.fortressType.getDelay() < System.currentTimeMillis()) {
                this.lastFire = System.currentTimeMillis();
                this.bombs.add(new Bomb(this.position.x, this.position.y, target, this.fortressType.getAP()));
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_fortress_attack.play();
                }
            }
        }
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

    public FortressType getType(){
        return  this.fortressType;
    }

}

