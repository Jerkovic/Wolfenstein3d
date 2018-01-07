package com.bbe.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.bbe.game.components.MeshComponent;
import com.bbe.game.components.TransformComponent;
import com.bbe.game.utils.Logger;


public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ComponentMapper<MeshComponent> mcm = ComponentMapper.getFor(MeshComponent.class);
    private ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(TransformComponent.class);
    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private Environment e;

    public RenderSystem(PerspectiveCamera camera, Environment e) {
        modelBatch = new ModelBatch();
        this.camera = camera;
        this.e = e;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(MeshComponent.class).get());
    }

    @Override
    public void update(float deltaTime)
    {
        MeshComponent meshComponent;
        TransformComponent transformComponent;

        Logger.log("Render model instances:" + entities.size());

        modelBatch.begin(camera);

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            meshComponent = mcm.get(entity);
            transformComponent = tcm.get(entity);

            meshComponent.model.transform.setToTranslation(transformComponent.position);
            modelBatch.render(meshComponent.model, e);

            Logger.log("RenderSystem render " + entity);
        }

        modelBatch.end();


    }


}
