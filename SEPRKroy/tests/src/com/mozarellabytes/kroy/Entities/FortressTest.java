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
import static org.junit.Assert.*;

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
    public void attackTruckFromCliffordFortressRangeTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(13, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(14, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressRangeAfterBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 4
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(14, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressRangeAfterBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangeTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressRangeAfterBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 10), FireTruckType.Speed);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
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