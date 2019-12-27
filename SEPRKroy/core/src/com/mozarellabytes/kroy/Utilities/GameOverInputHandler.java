package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.InputProcessor;
import com.mozarellabytes.kroy.Screens.GameOverScreen;

public class GameOverInputHandler implements InputProcessor {

    private final GameOverScreen gameOver;

    public GameOverInputHandler(GameOverScreen gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SoundFX.StopGameMusic();
        gameOver.toMenuScreen();
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
