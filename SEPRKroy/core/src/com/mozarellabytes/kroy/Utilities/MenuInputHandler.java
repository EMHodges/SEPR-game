package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.MenuScreen;

public class MenuInputHandler implements InputProcessor {

    private final MenuScreen menu;

    /** Constructs the MenuInputHandler
     *
     * @param menu the menu screen that this input handler is controlling
     */
    public MenuInputHandler(MenuScreen menu) {
        this.menu = menu;
    }

    /** Exits the game if 'ESCAPE' key is pressed, goes to control
     * screen if 'C' is pressed, toggles the sound if 'S' is pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
            case Input.Keys.C:
                menu.clickedControlsButton();
                menu.toControlScreen();
                break;
            case Input.Keys.S:
                menu.clickedSoundButton();
                menu.changeSound();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) {
            SoundFX.sfx_truck_attack.stop();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /** Checks if the user clicks on the start, controls or sound button.
     * It starts the game, shows the controls screen or toggles the sound
     * respectively.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return the input was processed */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = menu.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if (menu.getStartButton().contains(position.x, position.y)) {
            menu.clickedStartButton();
        } else if (menu.getControlsButton().contains(position.x, position.y)) {
            menu.clickedControlsButton();
        } else if (menu.getSoundButton().contains(position.x, position.y)) {
            menu.clickedSoundButton();
        }
        return true;
    }

    /** Executes the action according to the button clicked by the user.
     * i.e. if the user clicks down on the Start button but lifts their
     * click somewhere else, the game will not start.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = menu.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if (menu.getStartButton().contains(position.x, position.y)) {
            menu.toGameScreen();
        } else if (menu.getControlsButton().contains(position.x, position.y)) {
            menu.toControlScreen();
        } else if (menu.getSoundButton().contains(position.x, position.y)){
            menu.changeSound();
        } else {
            menu.idleStartButton();
            menu.idleControlsButton();
            menu.idleSoundButton();
        }
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
