package com.bbe.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bbe.game.components.MeshComponent;
import com.bbe.game.components.TransformComponent;

public class EntityFactory {

    public static void createPlayer(Engine engine, Vector3 pos, ModelInstance instance) {
        Entity entity = new Player(instance);
        entity.getComponent(TransformComponent.class).position.set(pos);
        entity.getComponent(MeshComponent.class).model.transform.setTranslation(pos);
        engine.addEntity(entity);
    }
}
