package com.mozarellabytes.kroy;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(GdxTestRunner.class)
public class GameStateTest {

    @Mock
    Kroy kroyMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * This class should pass if the exception is called, because this means
     * that GameOverScreen is called, therefore is trying to change screen
     */
    @Test(expected = NullPointerException.class)
    public void screenShouldChangeWhenAllTrucksDestroyedTest() {
        GameState gameState = new GameState();
        gameState.addFireTruck();
        gameState.removeFireTruck();
        gameState.hasGameEnded(kroyMock);
    }

    /**
     * this test should pass if it doesn't crash, this means that GameOverScreen
     * is not called therefore does not throw an exception
     */
    @Test
    public void screenShouldNotChangeWhenOneFortressDestroyedTest() {
        GameState gameState = new GameState();
        gameState.addFireTruck();
        gameState.addFortress();
        gameState.hasGameEnded(kroyMock);
    }

    /**
     * This class should pass if the exception is called, because this means
     * that GameOverScreen is called, therefore is trying to change screen
     */
    @Test(expected = NullPointerException.class)
    public void screenShouldChangeWhenAllThreeFortressesDestroyedTest() {
        GameState gameState = new GameState();
        gameState.addFortress();
        gameState.addFortress();
        gameState.addFortress();
        gameState.hasGameEnded(kroyMock);
    }

}