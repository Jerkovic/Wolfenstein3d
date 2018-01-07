package com.bbe.game.utils;

import com.badlogic.gdx.Gdx;

public class Logger {
    public static void log(String message) {
        Gdx.app.log("Wolfenstein", message);
    }
}
