package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.mozarellabytes.kroy.Entities.FireTruckType.Speed;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class FireTruckTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void TestTest(){
        assertEquals(1,1);
    }
    //@Before
    //        GameScreen gameScreen = new GameScreen(Kroy);
    //        FireTruck fireTruck = new FireTruck(gameScreen, float 0.0, float 0.0, FireTruckType type);

//    @Test
//   public void getPosition() {
//
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertTrue((fireTruck.getPosition()) instanceof Vector2);
//    }

//    @Test
//    public void getPath() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertTrue((fireTruck.getPath()) instanceof Queue<Vector2>);
//
//
//
//    }
//
//    @Test//dont know how to test
//    public void getTrailImage() {
//    }
//
//    @Test
//    public void getTrailImageEnd() {
//    }
//
//    @Test
//    public void addTileToPath() {//pseudo
//        Queue<Vector2> trailpathBefore = trailpath
//        path.addLast(new Vector2 (3,4))
//
//
//    }
//
//    @Test
//   public void resetTilePath() {
//            Queue<Vector2> trailpathBefore= trailpath;
//            path.resetTilePath();
//            assertEquals(trailpathBefore,path);
//    }
//
//    @Test
//    public void testisValidMove() {
//
//        float x =0.0f;
//
//        FireTruck fireTruck = new FireTruck(gameScreen, x,y, Speed);
//        Vector2 position2d = new Vector2(3,4);
//        Gamescreen gamescreen;
//        assertEquals(gamescreen.selectedtruck.isValidMove(position2d), true);
//    }
//
//    @Test
//    public void followPath() {
//    }
//
//    @Test
//    public void attack() {
//    }
//
//    @Test
//    public void damage() {
//    }
//
//    @Test
//    public void findFortressWithinRange() {
//    }
//
//    @Test
//    public void testRepair() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        float HPBefore = fireTruck.getHP();
//        repair(HPBefore);
//        float HPAfter = fireTruck.getHP();
//        assertTrue(HPBefore<HPAfter);
//    }
//
    @Test
    public void TestRefill() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        float reserveBefore = fireTruck.getReserve();
        fireTruck.refill(50);
        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore<reserveAfter);
    }
////    @Test
//    public void TestRefill() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//
//    }
//    @Test
//    public void testGetHP() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertTrue((fireTruck.getHP()) instanceof float);
//    }
//
//    @Test
//    public void testGetReserve() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertTrue((fireTruck.getReserve()) instanceof float);
//    }
//
//    @Test
//    public void fortressDamage() {
//    }
//
//    @Test
//    public void getMaxHP() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getMaxHP(),100);
//    }
//
//    @Test
//    public void testGetMaxReserve() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getMaxReserve(),100);
//    }
//
//    @Test
//    public void getName() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getName(),"Speed Truck");
//    }
//
//    @Test
//    public void testGetSpeed() {
//        Kroy kroy= new Kroy();
//        GameScreen gameScreen= new GameScreen(kroy);
//        this.x=0.0f;
//        this.y=0.0f;
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getSpeed(),2);
//    }
//
//    @Test
//    public void testGetAP() {
//        Kroy kroy= new Kroy();
//        GameScreen gameScreen= new GameScreen(kroy);
//        this.x=0.0f;
//        this.y=0.0f;
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getAP(),0.08f);
//    }
//
//    @Test
//    public void testGetRange() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getRange(),5);
//    }
}