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

    private final GameScreen gameScreen;
    private final GUI gui;

    public GameInputHandler(GameScreen gameScreen, GUI gui) {
        this.gameScreen = gameScreen;
        this.gui = gui;
    }

    /** Called when a key was pressed
     *
     * This handles toggling sound, the control screen, the pause
     * screen and makes the fire trucks attack a fortress that is
     * within it's range
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
            case Input.Keys.A:
                if (SoundFX.music_enabled && gameScreen.gameState.trucksInAttackRange > 0) {
                    SoundFX.sfx_truck_attack.loop();
                    SoundFX.sfx_truck_attack.play();
                }
                for (FireTruck truck: gameScreen.getStation().getTrucks()){
                    truck.setAttacking(true);
                }
                break;
            case Input.Keys.C:
                gameScreen.toControlScreen();
                break;
            case Input.Keys.S:
                gui.clickedSoundButton();
                gui.changeSound();
                gui.idleSoundButton();
                break;
            case Input.Keys.P:
                gui.clickedPauseButton();
                gameScreen.changeState();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            if (keycode == Input.Keys.A) {
                SoundFX.sfx_truck_attack.stop();
                for (FireTruck truck : gameScreen.getStation().getTrucks()) {
                    truck.setAttacking(false);
                }
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
            Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
            if (this.gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
                if (gameScreen.isRoad((int) clickCoordinates.x, (int) clickCoordinates.y)) {
                    if (gameScreen.checkClick(clickCoordinates)) {
                        gameScreen.selectedTruck.resetPath();
                        gameScreen.selectedTruck.addTileToPath(clickCoordinates);
                    } else if (!gameScreen.checkTrailClick(clickCoordinates) && !checkFortressClick(clickCoordinates)) {
                        gameScreen.selectedTruck = null;
                        gameScreen.selectedEntity = null;
                    }
                } else {
                    checkFortressClick(clickCoordinates);
                }
            }
            checkButtonClick(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
            return true;
        }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            if (gameScreen.selectedTruck != null) {
                Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
                gameScreen.selectedTruck.addTileToPath(clickCoordinates);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            if (gameScreen.selectedTruck != null) {
                if (!gameScreen.selectedTruck.trailPath.isEmpty()) {
                    checkSameLastTile();
                }
                gameScreen.selectedTruck.setMoving(true);
            }
        }
        checkButtonUnclick(screenX, screenY);
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

    private void checkSameLastTile(){
        for (FireTruck truck : gameScreen.getStation().getTrucks()) {
            if (!truck.equals(gameScreen.selectedTruck)) {
                if (doTrucksHaveSameLastTile(gameScreen.selectedTruck, truck)){
                    giveTrucksDifferentLastTiles(gameScreen.selectedTruck);
                }
            }
        }
    }

    private boolean doTrucksHaveSameLastTile(FireTruck selectedTruck, FireTruck truck2) {
        if (!truck2.getPath().isEmpty()){
            if (truck2.trailPath.last().equals(selectedTruck.trailPath.last())){
                return true;
            }
        } else if (truck2.getPosition().equals(selectedTruck.trailPath.last())) {
            return true;
        }
        return false;
    }

    private void giveTrucksDifferentLastTiles(FireTruck selectedTruck){
        selectedTruck.trailPath.removeLast();
        while (!selectedTruck.trailPath.isEmpty() && !selectedTruck.trailPath.last().equals(selectedTruck.path.last())) {
            selectedTruck.path.removeLast();
        }
    }


    private Vector2 generateClickCoordinates(int screenX, int screenY){
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.getCamera().unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        Vector2 position2d = new Vector2((int) position.x, (int) position.y);
        return position2d;
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
        for (Fortress fortress : gameScreen.getFortresses()) {
            if (fortress.getArea().contains(position2d)) {
                gameScreen.selectedEntity = fortress;
                return true;
            }
        }
        gameScreen.selectedTruck = null;
        gameScreen.selectedEntity = null;
        return false;
    }


    private boolean checkButtonUnclick(int screenX, int screenY){
        Vector2 screenCoords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

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
