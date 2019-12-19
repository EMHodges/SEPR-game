package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Screen;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.ControlsScreen;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.MenuScreen;

public class ScreenHandler {


    public static void ToMenu(Kroy game) {
        game.setScreen(new MenuScreen(game));
    }

    public static void ToGame(Kroy game) {
        game.setScreen(new GameScreen(game));
    }

    public static void ToControls(Kroy game, Screen parent, String screen) {
        game.setScreen(new ControlsScreen(game, parent, screen));
    }
}