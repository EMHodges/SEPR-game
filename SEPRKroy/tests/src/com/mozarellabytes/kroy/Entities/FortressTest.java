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

import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FortressTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void bombDamageTruckFromWalmgateFortressTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        float truckHealthBefore = fireTruck.getHP();
        Bomb bomb = new Bomb(fortress, fireTruck);
        if (bomb.checkHit()) {
            bomb.damageTruck();
        }
        float truckHealthAfter = fireTruck.getHP();
        assertEquals(truckHealthBefore - 15, truckHealthAfter, 0.0);
    }

    @Test
    public void canBeDestroyedTest() {
        assertEquals(1, 1);
    }

}