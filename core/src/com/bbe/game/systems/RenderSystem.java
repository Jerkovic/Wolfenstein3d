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


    private ComponentMapper<MeshComponent> pm = ComponentMapper.getFor(MeshComponent.class);
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
        // get the renderable Model instances
        Array<ModelInstance> instances = new Array<ModelInstance>();
        for(int i=0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            ModelInstance model = entity.getComponent(MeshComponent.class).model;
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            //Logger.log("Model Transform scale " + model.transform.getScaleX());
            model.transform.setToTranslation(transform.position);
            instances.add(model);
        }
        // end get

        Logger.log("Rendering model instances");
        modelBatch.begin(camera);
		    modelBatch.render(instances, e);
		modelBatch.end();

    }


}
