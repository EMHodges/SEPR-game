package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.mozarellabytes.kroy.Entities.FortressType.Clifford;
import static com.mozarellabytes.kroy.Entities.FortressType.Revs;
import static com.mozarellabytes.kroy.Entities.FortressType.Walmgate;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FortressTest {

    @Mock
    GameScreen gameScreenMock;

    @Test
    public void differentRangeTest() {
        assertTrue(Clifford.getRange() != Revs.getRange() && Revs.getRange() != Walmgate.getRange());
    }

    @Test
    public void differentMaxHPTest() {
        assertTrue(Clifford.getMaxHP() != Revs.getMaxHP() && Revs.getMaxHP() != Walmgate.getMaxHP());
    }

    @Test
    public void differentFireRateTest() {
        assertTrue(Clifford.getDelay() != Revs.getDelay() && Revs.getDelay() != Walmgate.getDelay());
    }

    @Test
    public void differentAPTest() {
        assertTrue(Clifford.getAP() != Revs.getAP() && Revs.getAP() != Walmgate.getAP());
    }

    @Test
    public void attackTruckFromWalmgateFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        new Bomb(fortress, fireTruck, true).damageTruck();
        assertEquals(135, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 1000);
        fortress.attack(fireTruck, false);
        fortress.updateBombs();
        assertEquals(130.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromRevolutionFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        new Bomb(fortress, fireTruck, true).damageTruck();
        assertEquals(140.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromWalmgateFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(19, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(13, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(14, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

}