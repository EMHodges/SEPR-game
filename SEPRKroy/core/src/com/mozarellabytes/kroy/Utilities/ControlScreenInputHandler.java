package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.ControlsScreen;

/**
 * This class controls the input for the Control screen
 * */
public class ControlScreenInputHandler implements InputProcessor {

    private final ControlsScreen controlsScreen;

    /**
     *  Constructs the control screen input handler
     *
     * @param controlsScreen the control screen that this input handler controls
     */
    public ControlScreenInputHandler(ControlsScreen controlsScreen) {

        this.controlsScreen = controlsScreen;
    }

    /** Called when a key was pressed
     *
     * Changes the screen to and from the controls screen when
     * 'C' is pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.C:
                controlsScreen.changeScreen();
                break;
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



    /** Called when the screen was touched or a mouse button was pressed.
     *
     * Goes back to the screen where the menu screen was called from when the
     * exit button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
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
