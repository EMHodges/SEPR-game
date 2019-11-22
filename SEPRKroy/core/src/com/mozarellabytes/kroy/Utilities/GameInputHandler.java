package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.io.Console;

public class GameInputHandler implements InputProcessor {

    private GameScreen gameScreen;

    public GameInputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
            case Input.Keys.R:
                gameScreen.station.spawn(0.2, "red");
                break;
            case Input.Keys.B:
                gameScreen.station.spawn(0.5, "blue");
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = gameScreen.camera.unproject(clickCoordinates);
        position = new Vector3((int) position.x, (int)position.y, 0);

        if (gameScreen.isRoad((int) position.x, (int) position.y)) {
            if (gameScreen.checkClick(position)) {
                gameScreen.activeTruck.resetTilePath();
                gameScreen.activeTruck.addTileToPath(position);
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // this is where the path is completed and the truck should start to follow the route
        if (gameScreen.activeTruck != null) {
            gameScreen.activeTruck.setMoving(true);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameScreen.activeTruck != null) {
            Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 position = gameScreen.camera.unproject(clickCoordinates);
            position = new Vector3(((int) position.x), ((int) position.y), 0);

            if (gameScreen.activeTruck.isValidMove(position)) {
                gameScreen.activeTruck.addTileToPath(position);


            }

        }


        return true;
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
