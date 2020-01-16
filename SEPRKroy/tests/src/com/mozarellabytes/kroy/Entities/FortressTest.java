package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FortressTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void attackTruckFromWalmgateFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        new Bomb(fortress, fireTruck, true).damageTruck();
        assertEquals(85.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromRevolutionFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        new Bomb(fortress, fireTruck, true).damageTruck();
        assertEquals(90.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.Speed);
        new Bomb(fortress, fireTruck, true).damageTruck();
        assertEquals(80.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressRangePassTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(13, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressRangeFailTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(14, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeTest() {
        Fortress fortress = new Fortress(6, 6, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(2.5f, 1.5f), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeBoundaryTest() {
        Fortress fortress = new Fortress(6, 6, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(2.5f, 1.5f), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeAfterBoundaryTest() {
        Fortress fortress = new Fortress(6, 6, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(2, 1), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangePassTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangeFailTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getVisualPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressDestroyPassTest() {
        ArrayList<Fortress> fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(10, 10, FortressType.Clifford));
        fortresses.get(0).damage(150);
        if (fortresses.get(0).getHP() <= 0) {
            fortresses.remove(fortresses.get(0));
        }
        assertEquals(new ArrayList<Fortress>(), fortresses);
    }

    @Test
    public void attackTruckFromCliffordFortressDestroyFailTest() {
        ArrayList<Fortress> fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(10, 10, FortressType.Clifford));
        fortresses.get(0).damage(100);
        if (fortresses.get(0).getHP() <= 0) {
            fortresses.remove(fortresses.get(0));
        }
        assertNotEquals(new ArrayList<Fortress>(), fortresses);
    }

}