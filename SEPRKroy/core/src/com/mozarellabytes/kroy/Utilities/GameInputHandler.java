package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
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
            case Input.Keys.A:
                Gdx.app.log("pressed A", "A pressed");
                for (FireTruck truck: gameScreen.station.getTrucks()){
                    if (truck.inFortresssRange()){
                        truck.attack();
                        Gdx.app.log("attacking fortress", String.valueOf(gameScreen.fortress.getHP()));
                    }
                }
                break;
            case Input.Keys.P:
                gameScreen.camShake.shakeIt(.2f);
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
                if (!gameScreen.checkTrailClick(position2d)) {
                    gameScreen.selectedTruck = null;
                    gameScreen.selectedEntity = null;
                }
            }
        } else {
            boolean selected = false;
            for (Fortress fortress : gameScreen.getFortresses()) {
                if (fortress.getArea().contains(position2d)) {
                    gameScreen.selectedEntity = fortress;
                    selected = true;
                }
            }
            if (!selected) {
                gameScreen.selectedTruck = null;
                gameScreen.selectedEntity = null;
            }
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
    // there's a bug somewhere here, if you draw a trucks path and then keep clicking on the end of it's trail path when the
    // truck reaches that tile it crashes and gives you a queue is empty error
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // this is to prevent trucks being on the same tiles
        if (gameScreen.selectedTruck != null) {
            for (FireTruck truck : gameScreen.station.getTrucks()) {
                if (!truck.equals(gameScreen.selectedTruck)) {
                    if (!gameScreen.selectedTruck.trailPath.isEmpty()) {
                        if (!truck.getPath().isEmpty() && truck.trailPath.last().equals(gameScreen.selectedTruck.trailPath.last())
                                || truck.getPosition().equals(gameScreen.selectedTruck.trailPath.last())) {
                            gameScreen.selectedTruck.trailPath.removeLast();
                            while (!gameScreen.selectedTruck.trailPath.last().equals(gameScreen.selectedTruck.path.last())) {
                                gameScreen.selectedTruck.path.removeLast();
                            }
                        }
                    }
                }
            }
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
