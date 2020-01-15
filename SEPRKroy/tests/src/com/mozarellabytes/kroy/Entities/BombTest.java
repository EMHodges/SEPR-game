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
    public void bombDamageTruckFromWalmgateFortressTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck);
        if (bomb.checkHit()) {
            bomb.damageTruck();
            assertEquals(85.0, fireTruck.getHP(), 0.0);
        }
    }

    @Test
    public void bombDamageTruckFromRevolutionFortressTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck);
        if (bomb.checkHit()) {
            bomb.damageTruck();
            assertEquals(90.0, fireTruck.getHP(), 0.0);
        }
    }

    @Test
    public void bombDamageTruckFromCliffordFortressTest() {
        Fortress fortress = new Fortress(11, 11, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.Speed);
        Bomb bomb = new Bomb(fortress, fireTruck);
        if (bomb.checkHit()) {
            bomb.damageTruck();
            assertEquals(80.0, fireTruck.getHP(), 0.0);
        }
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