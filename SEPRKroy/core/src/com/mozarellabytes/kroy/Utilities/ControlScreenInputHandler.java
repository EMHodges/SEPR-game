package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.ControlsScreen;
import com.mozarellabytes.kroy.Screens.MenuScreen;

public class ControlScreenInputHandler implements InputProcessor {


    private ControlsScreen controlsScreen;


    public ControlScreenInputHandler(ControlsScreen controlsScreen) {

        this.controlsScreen = controlsScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                controlsScreen.changeScreen();
                break;
            case Input.Keys.C:
                controlsScreen.changeScreen();
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
        Vector3 position = controlsScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if(controlsScreen.getExitButton().contains(position.x, position.y)){
            controlsScreen.changeScreen();
        }
        return false;
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
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
