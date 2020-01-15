package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.MenuScreen;

/** This class controls the input for the game over screen, it
 * is used when the game over screen is displayed and the player
 * has to press a button to return to the main screen.
 */

public class GameOverInputHandler implements InputProcessor {

    private Kroy game;

    /** Constructs the GameOverInputHandler
     * @param game needs the game to be able to return to the menu screen */
    public GameOverInputHandler(Kroy game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) {
            SoundFX.sfx_truck_attack.stop();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

     /** Stops the game music from playing and returns to the menu screen
     *
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SoundFX.StopGameMusic();
        game.setScreen(new MenuScreen(game));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
