package com.bbe.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bbe.game.Wolfenstein;
import com.bbe.game.utils.Logger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Wolfenstein(), config);

		info();
	}

	private static void info() {
		Logger.log("OS name: " + System.getProperty("os.name"));
		Logger.log("OS version: " + System.getProperty("os.version"));
		Logger.log("Gdx version: " + com.badlogic.gdx.Version.VERSION);

	}
}
