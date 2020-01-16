package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class BombTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void attackTruckFromWalmgateFortressDamageTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck, true);
        if (bomb.checkHit()) {
            bomb.damageTruck();
            assertEquals(85.0, fireTruck.getHP(), 0.0);
        }
    }

    @Test
    public void attackTruckFromRevolutionFortressDamageTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck, true);
        if (bomb.checkHit()) {
            bomb.damageTruck();
        }
        assertEquals(90.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressDamageTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck, true);
        if (bomb.checkHit()) {
            bomb.damageTruck();
        }
        assertEquals(80.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressRangePassTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(14, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressRangeFailTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangePassTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15.4f, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeFailTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangePassTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangeFailTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate); // range 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressDestroyPassTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate); // TODO
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressDestroyFailTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate); // TODO
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 11), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void reachesTargetMiss() {
    }

    @Test
    public void reachesTargetHit() {
    }

    @Test
    public void getTargetPos() {
    }
}