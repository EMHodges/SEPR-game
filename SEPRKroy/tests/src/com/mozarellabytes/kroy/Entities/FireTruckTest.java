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

import static com.mozarellabytes.kroy.Entities.FireTruckType.Ocean;
import static com.mozarellabytes.kroy.Entities.FireTruckType.Speed;
import static org.junit.Assert.*;


@RunWith(GdxTestRunner.class)
public class FireTruckTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testAddTileToPath() {
        FireTruck fireTruck=new FireTruck(gameScreenMock,new Vector2(10,10), Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        Queue<Vector2> expectedQueue = new Queue<Vector2>();
        for (float i=0; i< 1.1; i+=0.1){
            expectedQueue.addLast(new Vector2(10,10+i));
        }
        assertEquals(expectedQueue, fireTruck.getPath());
    }

    @Test
    public void testAttack() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.setAttacking(true);
        float reserveBefore=fireTruck.getReserve();
        fireTruck.attack(fortress);
        float reserveAfter=fireTruck.getReserve();
        assertTrue(reserveBefore > reserveAfter);
    }

    @Test
    public void repairPassTest() {
        FireStation station = new FireStation(10, 10);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), Speed));
        station.getTruck(0).fortressDamage(50);
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertTrue(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void repairIncorrectPositionTest() {
        FireStation station = new FireStation(10, 10);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(20, 10), Speed));
        station.getTruck(0).fortressDamage(50);
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertFalse(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void repairAlreadyFullyRepairedTest() {
        FireStation station = new FireStation(10, 10);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), Speed));
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertFalse(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void refillPassTest() {
        FireStation station = new FireStation(10, 10);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), Speed));
        station.getTruck(0).setAttacking(true);
        station.getTruck(0).attack(fortress);
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertTrue(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void refillIncorrectPositionTest() {
        FireStation station = new FireStation(10, 10);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(20, 10), Speed));
        station.getTruck(0).setAttacking(true);
        station.getTruck(0).attack(fortress);
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertFalse(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void refillAlreadyFullTest() {
        FireStation station = new FireStation(10, 10);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), Speed));
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertFalse(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void damageFortressWithSpeedByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        fireTruck.setAttacking(true);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fortressHealthAfter = fortress.getHP();
        assertEquals(FortressType.Walmgate.getMaxHP() - Speed.getAP(), fortressHealthAfter, 0.0);
    }

    @Test
    public void damageFortressWithSpeedByReserveTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        fireTruck.setAttacking(true);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fireTruckReserveAfter = fireTruck.getReserve();
        assertEquals(Speed.getMaxHP() - Speed.getAP(), fireTruckReserveAfter, 0.0);
    }

    @Test
    public void damageFortressWithOceanByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Ocean);
        fireTruck.setAttacking(true);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fortressHealthAfter = fortress.getHP();
        assertEquals(FortressType.Walmgate.getMaxHP() - Ocean.getAP(), fortressHealthAfter, 0.0);
    }

    @Test
    public void damageFortressWithOceanByReserveTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Ocean);
        fireTruck.setAttacking(true);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fireTruckReserveAfter = fireTruck.getReserve();
        assertEquals(Ocean.getMaxReserve() - Ocean.getAP(), fireTruckReserveAfter, 0.0);
    }

    @Test
    public void speedTruckShouldMove3TilesIn25FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        fireTruck.addTileToPath(new Vector2(11,11));
        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldNotMove3TilesIn25FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), Ocean);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        fireTruck.addTileToPath(new Vector2(11,11));
        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertNotEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldMove3TilesIn50FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), Ocean);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        fireTruck.addTileToPath(new Vector2(11,11));
        for (int i=0; i<50; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

}