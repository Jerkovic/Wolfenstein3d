package com.bbe.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.bbe.game.components.listener.TestListener;
import com.bbe.game.entities.EntityFactory;
import com.bbe.game.graphics.Light;
import com.bbe.game.systems.RenderSystem;
import com.bbe.game.systems.TransformSystem;
import com.bbe.game.utils.Logger;

import java.util.ArrayList;


public class Wolfenstein implements ApplicationListener {

	private Engine engine; // Ashley ecs
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private CameraInputController camController;
	public ArrayList<Light> lights = new ArrayList<Light>();
	// private FrameBuffer fbo;

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


		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		// Setup engine and register systems
		this.engine = new Engine();
		engine.addSystem(new TransformSystem());
		engine.addSystem(new RenderSystem(cam, environment));
		engine.addEntityListener(new TestListener());


		EntityFactory.createPlayer(engine, new Vector3(0,0,0), loadScene());

	}

	public ModelInstance loadScene() {
		final G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
		model = loader.loadModel(Gdx.files.internal("models/scene_f0.g3db"));
		ModelInstance modelInstance = new ModelInstance(model);
		modelInstance.transform.setToScaling(4f, 4f, 4f);
		return modelInstance;
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

		// Input ctrl should not be here!!
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

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
