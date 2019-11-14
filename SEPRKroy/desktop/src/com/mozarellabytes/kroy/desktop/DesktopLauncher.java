package com.mozarellabytes.kroy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mozarellabytes.kroy.Kroy;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 720;
		config.width = 1280;
		config.resizable = false;
		config.title = "Kroy";
		config.vSyncEnabled = true;
		config.forceExit = true;
		new LwjglApplication(new Kroy(), config);
	}
}
