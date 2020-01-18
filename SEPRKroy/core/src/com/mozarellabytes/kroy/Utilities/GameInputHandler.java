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

    /** The game screen that this input handler controls */
    private final GameScreen gameScreen;

    /** The graphical user interface - contains the buttons */
    private final GUI gui;

    /** Constructs the GameInputHandler
     *
     * @param gameScreen The game screen that this input handler controls
     * @param gui The graphical user interface - contains the buttons
     */
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
                System.out.println(gameScreen.gameState.getTrucksInAttackRange());
                if (SoundFX.music_enabled && gameScreen.gameState.getTrucksInAttackRange() > 0) {
                    SoundFX.playTruckAttack();
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
                SoundFX.stopTruckAttack();
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

    /** Checks whether the user clicks on a firetruck, fortress, button or the end
     * of a firetrucks path
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
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

    /** The user draws a path for the fire truck, if the path is valid the coordinate
     * positions are added to the trucks path
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
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

    /** Check if a user clicks up on a button or if the user draws multiple
     * trucks to end on the same tile
     *
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            if (gameScreen.selectedTruck != null) {
                if (!gameScreen.selectedTruck.trailPath.isEmpty()) {
                    if (doTrucksHaveSameLastTile()){
                        giveTrucksDifferentLastTiles(gameScreen.selectedTruck);
                    }
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

    /** Checks if the user has drawn more than one truck to the same end tile.
     *
     * @return <code> true </code> If more than one truck has the same end tile
     *      * <code> false </code> Otherwise
     */
    private boolean doTrucksHaveSameLastTile() {
        for (FireTruck truck : gameScreen.getStation().getTrucks()) {
            if (!truck.equals(gameScreen.selectedTruck)) {
                if (!truck.getPath().isEmpty() && !truck.getTrailPath().isEmpty()){
                    if (truck.trailPath.last().equals(gameScreen.selectedTruck.trailPath.last())){
                        return true;
                    }
                } else if (truck.getPosition().equals(gameScreen.selectedTruck.trailPath.last())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doTrucksHaveSameLastTile(FireTruck selectedTruck, FireTruck truck2) {
        if (!truck2.getTrailPath().isEmpty()){
            if (truck2.trailPath.last().equals(selectedTruck.trailPath.last())){
                return true;
            }
        } else if (truck2.getPosition().equals(selectedTruck.trailPath.last())) {
            return true;
        }
        return false;
    }

    /** The method for giving trucks that have the same end tiles adjacent end tiles
     * so that they do not end up on the same tile
     * @param selectedTruck the truck that has to be moved so the two trucks end up
     *                      on different tiles
     */
    private void giveTrucksDifferentLastTiles(FireTruck selectedTruck){
        selectedTruck.trailPath.removeLast();
        while (!selectedTruck.trailPath.isEmpty() && !selectedTruck.trailPath.last().equals(selectedTruck.path.last())) {
            selectedTruck.path.removeLast();
        }
    }

    /** Maps the position of where the user clicked on the screen to the tile that they clicked on
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return a Vector2 containing the tile that the user clicked on
     */
    private Vector2 generateClickCoordinates(int screenX, int screenY){
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.getCamera().unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        Vector2 position2d = new Vector2((int) position.x, (int) position.y);
        return position2d;
    }


    /** Checks if the user clicked on the home, pause or sound button
     * and changes the sprite accordingly
     * @param position2d The tile that was clicked
     * @return <code> true </code> If a button has been clicked
     *         <code> false </code> Otherwise
     */
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

    /** Checks if user clicked on a fortress, if it did this fortress
     * becomes the selected entity meaning its stats will be rendered
     * in the top right hand corner
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a fortress has been clicked on
     *         <code> false </code> Otherwise
     */
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

    /** Checks if the user has lifted the mouse over a button and triggers the
     * appropriate action
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return <code> true </code> If the user has clicked up over a button
     *         <code> false </code> Otherwise
     */
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
