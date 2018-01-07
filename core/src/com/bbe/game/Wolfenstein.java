package com.bbe.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.bbe.game.components.listener.TestListener;
import com.bbe.game.entities.EntityFactory;
import com.bbe.game.systems.RenderSystem;
import com.bbe.game.systems.TransformSystem;
import com.bbe.game.utils.Logger;

public class Wolfenstein implements ApplicationListener {

	private Engine engine; // Ashley ecs
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private CameraInputController camController;

	@Override
	public void create () {
		Logger.log("Creating Wolfenstein...");

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(6f, 6f, 6f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		modelBatch = new ModelBatch();


		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(3f, 3f, 3f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);



		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);


		// Setup engine and register systems
		this.engine = new Engine();
		engine.addSystem(new TransformSystem());
		engine.addSystem(new RenderSystem(cam, environment));
		engine.addEntityListener(new TestListener());

		// Create entities
		for (float i = 0; i < 1; i++) {
			instance = new ModelInstance(model, 3f, 0f, 0f);
			EntityFactory.createPlayer(engine, new Vector3(i * 15f,0,0), instance);
			instance = null;
		}

	}

	@Override
	public void resize(int width, int height) {
		Logger.log("Window resized");
	}

	public void update() {
		// update all systems registered in engine
		engine.update(Gdx.graphics.getDeltaTime());
		camController.update();
	}

	@Override
	public void render () {

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		update();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

		modelBatch.dispose();
		model.dispose();
	}
}
