package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.State;

public class GameInputHandler implements InputProcessor {

    private GameScreen gameScreen;
    private GUI gui;

    public GameInputHandler(GameScreen gameScreen, GUI gui) {
        this.gameScreen = gameScreen;
        this.gui = gui;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
            case Input.Keys.A:
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_truck_attack.loop();
                    SoundFX.sfx_truck_attack.play();
                }
                for (FireTruck truck: gameScreen.station.getTrucks()){
                    truck.setAttacking(true);
                }
                break;
            case Input.Keys.C:
                gameScreen.toControlScreen();
                break;
            case Input.Keys.B:
                gameScreen.camShake.shakeIt(.2f);
                break;
            case Input.Keys.F:
                SoundFX.music_enabled = false;
                SoundFX.StopMusic();
                break;
            case Input.Keys.G:
                SoundFX.music_enabled = true;
                SoundFX.PlayMusic();
                break;
            case Input.Keys.P:
                gui.clickedPauseButton();
                gameScreen.changeState();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (this.gameScreen.getState().equals(State.PLAY)) {
            switch (keycode) {
                case Input.Keys.A:
                    SoundFX.sfx_truck_attack.stop();
                    for (FireTruck truck : gameScreen.station.getTrucks()) {
                        truck.setAttacking(false);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        Vector2 position2d = new Vector2((int) position.x, (int) position.y);
        if (this.gameScreen.getState().equals(State.PLAY)) {
            if (gameScreen.isRoad((int) position2d.x, (int) position2d.y)) {
                if (gameScreen.checkClick(position2d)) {
                    gameScreen.selectedTruck.resetPath();
                    gameScreen.selectedTruck.addTileToPath(position2d);
                } else {
                    if (!gameScreen.checkTrailClick(position2d) && !checkFortressClick(position2d)) {
                        gameScreen.selectedTruck = null;
                        gameScreen.selectedEntity = null;
                    }
                }
            } else {
                checkFortressClick(position2d);
            }
        }
        checkButtonClick(new Vector2(clickCoordinates.x, Gdx.graphics.getHeight() - clickCoordinates.y));

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.gameScreen.getState().equals(State.PLAY)) {
            if (gameScreen.selectedTruck != null) {
                Vector2 clickCoordinates = new Vector2(screenX, screenY);
                Vector3 position = gameScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
                Vector2 position2d = new Vector2((int) position.x, (int) position.y);
                if (gameScreen.selectedTruck.isValidMove(position2d)) {
                    gameScreen.selectedTruck.addTileToPath(position2d);
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // this is to prevent trucks being on the same tiles
        if (this.gameScreen.getState().equals(State.PLAY)) {
            if (gameScreen.selectedTruck != null) {
                for (FireTruck truck : gameScreen.station.getTrucks()) {
                    if (!truck.equals(gameScreen.selectedTruck)) {
                        if (!gameScreen.selectedTruck.trailPath.isEmpty()) {
                            if (!truck.getPath().isEmpty() && truck.trailPath.last().equals(gameScreen.selectedTruck.trailPath.last())
                                    || truck.getPosition().equals(gameScreen.selectedTruck.trailPath.last())) {
                                gameScreen.selectedTruck.trailPath.removeLast();
                                while (!gameScreen.selectedTruck.trailPath.isEmpty() && !gameScreen.selectedTruck.trailPath.last().equals(gameScreen.selectedTruck.path.last())) {
                                    gameScreen.selectedTruck.path.removeLast();
                                }
                            }
                        }
                    }
                }
                gameScreen.selectedTruck.setMoving(true);
            } else {
                checkButtonUnclick(screenX, screenY);
            }
        } else {
            checkButtonUnclick(screenX, screenY);
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

    private boolean checkButtonClick(Vector2 position2d){
        if (gui.getHomeButton().contains(position2d)) {
            gui.clickedHomeButton();
        } else if (gui.getPauseButton().contains(position2d)){
            gui.clickedPauseButton();
        } else if (gui.getSoundButton().contains(position2d)) {
            gui.clickedSoundButton();
        }
        return true;
    }

    private boolean checkFortressClick(Vector2 position2d) {
        boolean selected = false;
        for (Fortress fortress : gameScreen.getFortresses()) {
            if (fortress.getArea().contains(position2d)) {
                gameScreen.selectedEntity = fortress;
                selected = true;
            }
        }
        if (selected) {
            return true;
        } else {
            gameScreen.selectedTruck = null;
            gameScreen.selectedEntity = null;
            return false;
        }
    }

    private boolean checkButtonUnclick(int screenX, int screenY){
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector2 screenCoords = new Vector2(clickCoordinates.x, Gdx.graphics.getHeight() - clickCoordinates.y);
        if (gui.getHomeButton().contains(screenCoords)) {
            gameScreen.toHomeScreen();
        } else {
            gui.idleHomeButton();
        }

        if (gui.getSoundButton().contains(screenCoords)){
            gui.changeSound();
        } else {
            gui.idleSoundButton();
        }

        if (gui.getPauseButton().contains(screenCoords)){
            gameScreen.changeState();
        } else {
            gui.idlePauseButton();
        }
        return true;
    }
}
