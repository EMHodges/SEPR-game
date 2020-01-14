package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Screen;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.ControlsScreen;
import com.mozarellabytes.kroy.Screens.GameOverScreen;
import com.mozarellabytes.kroy.Screens.MenuScreen;

/** This class is used to control the movement between screens */

public class ScreenHandler {

    /** Changes the screen to the menu screen
     *
     * @param game LibGdx game
     */
    public static void ToMenu(Kroy game) {
        game.setScreen(new MenuScreen(game));
    }

    /** Changes the screen to the game over screen
     *
     * @param game LibGdx game
     * @param b <code> true </code> if the player has won the game
     *          <code> false </code> if the player has lost the game
     */
    public static void ToGameOverScreen(Kroy game, Boolean b){
        game.setScreen(new GameOverScreen(game, b));
    }

    /** Changes the current screen to the control screen
     *
     * @param game LibGdx game
     * @param parent the screen that called to controls, so it can return to
     *               this screen after the controls screen has been exited
     * @param screen the name of the screen that called the controls screen
     */
    public static void ToControls(Kroy game, Screen parent, String screen) {
        game.setScreen(new ControlsScreen(game, parent, screen));
    }
}