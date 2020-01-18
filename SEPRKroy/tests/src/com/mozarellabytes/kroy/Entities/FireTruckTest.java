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
    public void differentSpeedTest() {
        assertNotEquals(Ocean.getSpeed(), Speed.getSpeed());
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

    @Test
    public void differentMaxVolumeTest() {
        assertNotEquals(Ocean.getMaxReserve(), Speed.getMaxReserve());
    }

    @Test
    public void differentAPTest() {
        assertNotEquals(Ocean.getAP(), Speed.getAP());
    }

    @Test
    public void checkTrucksFillToDifferentLevels() {
        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(9,10), Speed);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10,10), Ocean);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireStation fireStation = new FireStation(8, 10);
        fireStation.spawn(fireTruck1);
        fireStation.spawn(fireTruck2);
        fireTruck1.setAttacking(true);
        fireTruck2.setAttacking(true);
        for (int i=0; i<2000; i++) {
            fireTruck1.attack(fortress);
            fireTruck1.updateSpray();
            fireTruck2.attack(fortress);
            fireTruck2.updateSpray();
        }
        float fireTruck1ReserveEmpty = fireTruck1.getReserve();
        float fireTruck2ReserveEmpty = fireTruck2.getReserve();

        for (int i=0; i<2000; i++) {
            fireStation.restoreTrucks();
        }

        boolean checkEmptyReservesAreSame = fireTruck1ReserveEmpty == fireTruck2ReserveEmpty;
        boolean checkSpeedTruckIsFull = fireTruck1.getReserve() == Speed.getMaxReserve();
        boolean checkOceanTruckIsNotFull = fireTruck2.getReserve() !=  Ocean.getMaxReserve();

        assertTrue(checkEmptyReservesAreSame && checkSpeedTruckIsFull && checkOceanTruckIsNotFull);

    }

    @Test
    public void differentMaxHPTest() {
        assertNotEquals(Ocean.getMaxHP(), Speed.getMaxHP());
    }

    @Test
    public void checkTrucksRepairToDifferentLevels() {
        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(9,10), Speed);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10,10), Ocean);
        FireStation fireStation = new FireStation(8, 10);
        fireStation.spawn(fireTruck1);
        fireStation.spawn(fireTruck2);
        fireTruck1.repair(Speed.getMaxHP()*-1);
        fireTruck2.repair(Ocean.getMaxHP()*-1);
        float fireTruck1Health0 = fireTruck1.getHP();
        float fireTruck2Health0 = fireTruck2.getHP();

        for (int i=0; i<3000; i++) {
            fireStation.restoreTrucks();
        }

        boolean checkHealth0IsSame = fireTruck1Health0 == fireTruck2Health0;
        boolean checkOceanTruckIsFullyRepaired = fireTruck2.getHP() == Ocean.getMaxHP();
        boolean checkSpeedTruckIsNotFullyRepaired = fireTruck1.getHP() !=  Speed.getMaxHP();

        assertTrue(checkHealth0IsSame && checkOceanTruckIsFullyRepaired && checkSpeedTruckIsNotFullyRepaired);

    }

    @Test
    public void differentRangeTest() {
        assertNotEquals(Ocean.getRange(), Speed.getRange());
    }

    @Test
    public void checkDifferentRangeTest() {
        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(10, 17), Speed);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10, 17), Ocean);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck1.fortressInRange(fortress.getPosition());
        assertNotEquals(fireTruck1.fortressInRange(fortress.getPosition()), fireTruck2.fortressInRange(fortress.getPosition()));
    }

    @Test
    public void truckShouldDecreaseHealthOfFortress() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.setAttacking(true);
        float healthBefore = fortress.getHP();
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float healthAfter = fortress.getHP();
        assertTrue(healthBefore > healthAfter);
    }

    @Test
    public void truckShouldDecreaseReserveWhenAttackingFortress() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), Speed);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.setAttacking(true);
        float reserveBefore = fireTruck.getReserve();
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore > reserveAfter);
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
        assertEquals(Speed.getMaxReserve() - Speed.getAP(), fireTruckReserveAfter, 0.0);
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
    public void moveTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), Speed);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPath(new Vector2(10,10));
        fireTruck.addTileToPath(new Vector2(10,11));
        for (int i=0; i<50; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(10, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

}