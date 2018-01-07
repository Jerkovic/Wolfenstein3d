package com.bbe.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.bbe.game.components.MeshComponent;
import com.bbe.game.components.TransformComponent;


public class Player extends Entity {

    public Player(ModelInstance model) {
        super();
        add(new TransformComponent());
        add(new MeshComponent(model));

    }
}
