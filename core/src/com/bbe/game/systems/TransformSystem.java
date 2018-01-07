package com.bbe.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.bbe.game.components.TransformComponent;
import com.bbe.game.utils.Logger;


public class TransformSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);

    public TransformSystem() {}

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class).get());
    }

    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            TransformComponent transform = pm.get(entity);
            Logger.log("TransformSystem updating " + entity);
        }
    }
}