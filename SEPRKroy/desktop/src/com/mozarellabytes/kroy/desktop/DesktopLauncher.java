package com.mozarellabytes.kroy.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Constants.GAME_HEIGHT;
		config.width = Constants.GAME_WIDTH;
		config.resizable = false;
		config.title = "Kroy";
		config.vSyncEnabled = true;
		config.forceExit = true;
		config.useHDPI = true;

		new LwjglApplication(new Kroy(), config);
	}
}
