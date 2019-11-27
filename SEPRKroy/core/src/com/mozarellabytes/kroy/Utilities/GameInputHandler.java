package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.GameScreen;

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
                gameScreen.station.spawn("red");
                break;
            case Input.Keys.B:
                gameScreen.station.spawn("blue");
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
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        Vector2 position2d = new Vector2((int) position.x, (int)position.y);

        if (gameScreen.isRoad((int) position2d.x, (int) position2d.y)) {
            if (gameScreen.checkClick(position2d)) {
                gameScreen.selectedTruck.resetTilePath();
                gameScreen.selectedTruck.addTileToPath(position2d);
            } else {
                gameScreen.checkTrailClick(position2d);
            }
        } else {
            gameScreen.selectedTruck = null;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameScreen.selectedTruck != null) {
            Vector2 clickCoordinates = new Vector2(screenX, screenY);
            Vector3 position = gameScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
            Vector2 position2d = new Vector2((int) position.x, (int)position.y);
            if (gameScreen.selectedTruck.isValidMove(position2d)) {
                gameScreen.selectedTruck.addTileToPath(position2d);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // this is where the path is completed and the truck should start to follow the route
        if (gameScreen.selectedTruck != null) {
            gameScreen.selectedTruck.setMoving(true);
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
