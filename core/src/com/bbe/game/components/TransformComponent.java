package com.bbe.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public final Vector3 position = new Vector3();
    public final Vector3 scale = new Vector3(1.0f, 1.0f, 1.0f);
    public float rotation = 0.0f;
    public boolean isHidden = false;
}