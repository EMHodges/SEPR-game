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
    public void canBeDestroyed() {
        ArrayList<Fortress> fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(10, 10, FortressType.Walmgate));
        fortresses.get(0).damage(FortressType.Walmgate.getMaxHP());
        if (fortresses.get(0).getHP() <= 0) {
            fortresses.remove(fortresses.get(0));
        }
        assertEquals(new ArrayList<Fortress>(), fortresses);
    }

    @Test
    public void canBeDamaged() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fortress.damage(50);
        assertEquals(100, fortress.getHP(), 0.0);
    }

}