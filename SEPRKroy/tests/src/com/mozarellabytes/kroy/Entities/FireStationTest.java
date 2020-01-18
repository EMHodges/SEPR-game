package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
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
public class FireStationTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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
    public void trucksCannotOccupySameTileTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,12);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,13);

        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(11, 11), Speed);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(11, 13), Ocean);

        FireStation station = new FireStation(0,0);
        station.spawn(fireTruck1);
        station.spawn(fireTruck2);

        fireTruck1.setMoving(true);
        fireTruck1.addTileToPath(new Vector2(11, 11));
        fireTruck1.addTileToPath(new Vector2(11, 12));
        fireTruck1.addTileToPath(new Vector2(11, 13));
        for (int i=0; i<100; i++) {
            station.checkForCollisions();
            fireTruck1.move();
        }
        Vector2 expectedPosition = new Vector2(11, 12);
        assertEquals(expectedPosition, fireTruck1.getPosition());
    }

    @Test
    public void trucksShouldNotMovePastEachOtherTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,12);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,13);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,14);

        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(11, 11), Speed);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(11, 14), Ocean);

        FireStation station = new FireStation(0,0);
        station.spawn(fireTruck1);
        station.spawn(fireTruck2);

        fireTruck1.setMoving(true);
        fireTruck1.addTileToPath(new Vector2(11, 11));
        fireTruck1.addTileToPath(new Vector2(11, 12));
        fireTruck1.addTileToPath(new Vector2(11, 13));
        fireTruck1.addTileToPath(new Vector2(11, 14));

        fireTruck2.setMoving(true);
        fireTruck2.addTileToPath(new Vector2(11, 14));
        fireTruck2.addTileToPath(new Vector2(11, 13));
        fireTruck2.addTileToPath(new Vector2(11, 12));
        fireTruck2.addTileToPath(new Vector2(11, 11));
        for (int i=0; i<100; i++) {
            station.checkForCollisions();
            fireTruck1.move();
            fireTruck2.move();
        }
        Vector2 fireTruck1TargetPosition = new Vector2(11, 14);
        Vector2 fireTruck2TargetPosition = new Vector2(11, 11);
        assertTrue(!fireTruck1.getPosition().equals(fireTruck1TargetPosition) && !fireTruck2.getPosition().equals(fireTruck2TargetPosition));
    }

}