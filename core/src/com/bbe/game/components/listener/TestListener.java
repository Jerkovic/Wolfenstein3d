package com.bbe.game.components.listener;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.bbe.game.utils.Logger;

public class TestListener implements EntityListener {
    @Override
    public void entityAdded(Entity entity) {
        Logger.log(entity + " added to engine");
    }

    @Override
    public void entityRemoved(Entity entity) {
        Logger.log(entity + " removed from engine");
    }
}
