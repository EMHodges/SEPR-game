package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.MenuScreen;

public class MenuInputHandler implements InputProcessor {

    private MenuScreen menu;

    public MenuInputHandler(MenuScreen menu) {
        this.menu = menu;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
            case Input.Keys.C:
                menu.toControlScreen();
        }
        return true;
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
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = menu.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if (menu.getPlayButton().contains(position.x, position.y)) {
            menu.toGameScreen();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = menu.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if (menu.getPlayButton().contains(position.x, position.y)) {
            menu.hoverPlayButton();
            return true;
        } else {
            menu.idlePlayButton();
            return false;
        }
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
