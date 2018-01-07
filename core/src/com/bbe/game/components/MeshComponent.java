package com.bbe.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class MeshComponent implements Component {
    public ModelInstance model;

    public MeshComponent(ModelInstance model) {
        this.model = model;
    }
}
