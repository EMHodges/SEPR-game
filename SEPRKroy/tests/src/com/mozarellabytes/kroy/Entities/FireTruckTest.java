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
        FireTruck fireTruck=new FireTruck(gameScreenMock,new Vector2(10,10),Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        fireTruck.setMoving(true);
        Queue<Vector2> pathBefore=fireTruck.path;
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        Queue<Vector2> expectedQueue=new Queue<Vector2>();
        fireTruck.move();
        fireTruck.move();
        fireTruck.move();
        for(float i=0;i< 1.1;i+=0.1){
            expectedQueue.addLast(new Vector2(10,10+i));
        }
        System.out.println(fireTruck.getPosition());
        assertEquals(expectedQueue, fireTruck.getPath());
    }



    @Test
   public void testGetPosition() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        assertTrue((fireTruck.getPosition()) instanceof Vector2);
        Vector2 position=fireTruck.getPosition();
        assertTrue(position.epsilonEquals(10,10));;
    }

    
    @Test
    public void testAddTileToPath(){//how does it work?(path,interpolation etc)
        FireTruck fireTruck=new FireTruck(gameScreenMock,new Vector2(10,10),Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Queue<Vector2> pathBefore=fireTruck.path;
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        Queue<Vector2> expectedQueue=new Queue<Vector2>();
        for(float i=0;i< 1.1;i+=0.1){
            expectedQueue.addLast(new Vector2(10,10+i));
        }

        assertEquals(expectedQueue, fireTruck.getPath());
        }
//
    @Test
   public void testResetPath() {
            FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
            Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
            fireTruck.addTileToPath(new Vector2(10,10));
            fireTruck.resetPath();
            assertTrue((fireTruck.path).isEmpty());
    }


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






}