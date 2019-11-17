package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.Stack;

public class GameInputHandler implements InputProcessor {

    private GameScreen gameScreen;

    public GameInputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                gameScreen.truck.setMoveUp(true);
                break;
            case Input.Keys.A:
                gameScreen.truck.setMoveLeft(true);
                break;
            case Input.Keys.S:
                gameScreen.truck.setMoveDown(true);
                break;
            case Input.Keys.D:
                gameScreen.truck.setMoveRight(true);
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
        }
        return true;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                gameScreen.truck.setMoveUp(false);
                break;
            case Input.Keys.A:
                gameScreen.truck.setMoveLeft(false);
                break;
            case Input.Keys.S:
                gameScreen.truck.setMoveDown(false);
                break;
            case Input.Keys.D:
                gameScreen.truck.setMoveRight(false);
                break;
        }
        return true;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = gameScreen.camera.unproject(clickCoordinates);
        gameScreen.truck.setPosition(((int) (position.x * Constants.TILE_WxH)), (int) (position.y * Constants.TILE_WxH)-(48f+24f));
        Gdx.app.log("Player", gameScreen.truck.getX() + ", " + gameScreen.truck.getY());
        return true;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
