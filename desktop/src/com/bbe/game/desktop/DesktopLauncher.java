package com.bbe.game.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bbe.game.Wolfenstein;
import com.bbe.game.utils.Logger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;
		config.resizable = false;
		config.useGL30 = false;
		config.title = "Wolfenstein";
		config.vSyncEnabled = true;
		config.forceExit = true;
		if (config.fullscreen) {
			Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
			config.setFromDisplayMode(displayMode);
		}
		// Window icons: 128x128(Mac), 32x32 (for Win and Linux), and 16x16 (for Win).
		new LwjglApplication(new Wolfenstein(), config);

		info();
	}

	private static void info() {
		Logger.log("OS name: " + System.getProperty("os.name"));
		Logger.log("OS version: " + System.getProperty("os.version"));
		Logger.log("Gdx version: " + com.badlogic.gdx.Version.VERSION);
		Graphics.DisplayMode[] modes = LwjglApplicationConfiguration.getDisplayModes();
		Logger.log("======================Graphic Modes=========================");
		for (Graphics.DisplayMode mode: modes) {
			Logger.log("" + mode);
		}
		Logger.log("============================================================");


	}
}
