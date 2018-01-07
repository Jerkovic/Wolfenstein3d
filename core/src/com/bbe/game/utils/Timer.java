package com.bbe.game.utils;

import com.badlogic.gdx.utils.ObjectMap;

public class Timer {
    private ObjectMap<String, Long> times;

    public Timer() {
        times = new ObjectMap<String, Long>();
    }

    /**
     * Start tracking a time with name as id.
     *
     * @param name The timer's id
     */
    public void start(String name) {
        times.put(name, System.currentTimeMillis());
    }

    /**
     * Stop tracking the specified id
     *
     * @param name The timer's id
     * @return the elapsed time
     */
    public long stop(String name) {
        if (times.containsKey(name)) {
            long startTime = times.remove(name);
            return System.currentTimeMillis() - startTime;
        } else
            throw new RuntimeException("Timer id doesn't exist.");
    }
}