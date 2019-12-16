package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;

import static com.mozarellabytes.kroy.Entities.FireTruckType.Speed;
import static org.junit.Assert.*;

public class FireTruckTest {


    private float x;
    private float y;

    //@Before
    //        GameScreen gameScreen = new GameScreen(Kroy);
    //        FireTruck fireTruck = new FireTruck(gameScreen, float 0.0, float 0.0, FireTruckType type);

    @Test
    public void getPosition() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertTrue((fireTruck.getPosition()) instanceof Vector2);
    }

    @Test
    public void getPath() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertTrue((fireTruck.getPath()) instanceof Queue<Vector2>);



    }

    @Test//dont know how to test
    public void getTrailImage() {
    }

    @Test
    public void getTrailImageEnd() {
    }

    @Test
    public void addTileToPath() {//pseudo
        Queue<Vector2> trailpathBefore = trailpath
        path.addLast(new Vector2 (3,4))


    }

    @Test
    public void resetTilePath() {
    }

    @Test
    public void testisValidMove() {

//        GameScreen gameScreen = new GameScreen(Kroy);
//        FireTruck fireTruck = new FireTruck(GameScreen gameScreen, float 0.0, float 0.0, FireTruckType type);
//        Vector2 position2d = new Vector2(3,4);
//
//        assertEquals(gamescreen.selectedtruck.isValidMove(position2d), true);
    }

    @Test
    public void followPath() {
    }

    @Test
    public void attack() {
    }

    @Test
    public void damage() {
    }

    @Test
    public void findFortressWithinRange() {
    }

    @Test
    public void testRepair() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        float HPBefore = fireTruck.getHP();
        fireTruck.repair();
        float HPAfter = fireTruck.getHP();
        assertTrue(HPBefore<HPAfter);
    }

    @Test
    public void testRefill() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        float reserveBefore = fireTruck.getReserve();
        fireTruck.refill();
        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore<reserveAfter);
    }

    @Test
    public void testGetHP() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertTrue((fireTruck.getHP()) instanceof float);
    }

    @Test
    public void testGetReserve() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertTrue((fireTruck.getReserve()) instanceof float);
    }

    @Test
    public void fortressDamage() {
    }

    @Test
    public void getMaxHP() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getMaxHP(),100);
    }

    @Test
    public void testGetMaxReserve() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getMaxReserve(),100);
    }

    @Test
    public void getName() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getName(),"Speed Truck");
    }

    @Test
    public void testGetSpeed() {
        Kroy kroy= new Kroy();
        GameScreen gameScreen= new GameScreen(kroy);
        this.x=0.0f;
        this.y=0.0f;
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getSpeed(),2);
    }

    @Test
    public void testGetAP() {
        Kroy kroy= new Kroy();
        GameScreen gameScreen= new GameScreen(kroy);
        this.x=0.0f;
        this.y=0.0f;
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getAP(),0.08f);
    }

    @Test
    public void testGetRange() {
        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
        assertEquals(fireTruck.getRange(),5);
    }
}