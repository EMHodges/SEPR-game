package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import java.util.ArrayList;

import static com.mozarellabytes.kroy.Entities.FireTruckType.Speed;
import static org.junit.Assert.*;

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
//    @Before
//    FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);

    @Test
    public void testMove(){

    }

    @Test
   public void testGetPosition() {

        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        assertTrue((fireTruck.getPosition()) instanceof Vector2);
        Vector2 position=fireTruck.getPosition();
        assertTrue(position.epsilonEquals(10,10));;
    }

    @Test
    public void getPath() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        assertTrue((fireTruck.getTrailPath()) != null);
    }
//
//    @Test//dont know how to test
//    public void getTrailImage() {
//    }
//
//    @Test
//    public void getTrailImageEnd() {
//    }
//
    @Test
    public void testAddTileToPath() {//how does it work?(path,interpolation etc)
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10, 10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10, 11);
        Queue<Vector2> pathBefore=fireTruck.path;
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        Queue<Vector2> expectedQueue = new Queue<Vector2>();
        for (float i = 0; i < 1.1; i+=Speed.getSpeed()) {
            expectedQueue.addLast(new Vector2(10, 10 + i));
        }
        assertEquals(expectedQueue, fireTruck.getPath());
    }
//
//    @Test
//   public void testResetPath() {
//            FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
//            fireTruck.trailPath.addLast(new Vector2(((int) 10), ((int) 10)));
//            fireTruck.path.addLast(new Vector2(((int) 10), ((int) 10)));
//            fireTruck.resetPath();
//            assertTrue(fireTruck.path.size() == 0);
//            assertEquals(fireTruck.trailPath,[]);
//    }
//
//    @Test
//    public void testisValidMove() {
//       FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
//        Vector2 position2d = new Vector2(3,4);
//        assertEquals(fireTruck.isValidDraw(position2d), true);
//    }
//
//    @Test
//    public void followPath() {
//    }
//
    @Test
    public void testAttack() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.setAttacking(true);
        float reserveBefore=fireTruck.getReserve();
        fireTruck.attack(fortress);
        float reserveAfter=fireTruck.getReserve();
        assertTrue(reserveBefore>reserveAfter);
    }

//    @Test
//    public void damage() {//waterparticles?
//    }
//

    @Test
    public void testRepair() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        float HPBefore = fireTruck.getHP();
        fireTruck.repair(2);
        float HPAfter = fireTruck.getHP();
        assertTrue(HPBefore==HPAfter-2);
    }

    @Test
    public void TestRefill() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        for (int i=0; i<100; i++) {
            fireTruck.attack(new Fortress(10, 10, FortressType.Walmgate));
        }
        float reserveBefore = fireTruck.getReserve();
        fireTruck.refill(2);
        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore<reserveAfter);
    }

//    @Test
//    public void testGetHP() {
//        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
//        assertTrue(fireTruck.getHP() instanceof float);
//    }
//
//    @Test
//    public void testGetReserve() {what to test?
//        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
//        assertTrue((fireTruck.getReserve()) != null);
//    }
//
//
//    @Test
//    public void getMaxHP() {
//        FireTruck fireTruck = new FireTruck(gameScreen, x, y, Speed);
//        assertEquals(fireTruck.getMaxHP(),100);
//    }
//
//    @Test
//    public void testGetMaxReserve() {//firetrucktype how to get maxreserve? assertequals double double is deprecated?
//        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
//        System.out.println(Speed.getMaxReserve());
//        assertEquals(Speed.getMaxReserve(),100);
//    }
//
//    @Test
//    public void getName() {//how
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