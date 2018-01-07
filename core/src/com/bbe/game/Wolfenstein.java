package com.bbe.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.bbe.game.graphics.*;
import com.bbe.game.utils.Logger;
import com.bbe.game.utils.ScreenshotFactory;

import java.util.ArrayList;


public class Wolfenstein implements ApplicationListener {

	private Engine engine; // Ashley ecs
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private ShaderProgram shaderProgram;
	private CameraInputController camController;
	public ArrayList<Light> lights = new ArrayList<Light>();
	// Shadow stuff
	private ShaderProgram shaderProgramShadows;
	private FrameBuffer frameBufferShadows;
	private ModelBatch modelBatchShadows;

	@Override
	public void create () {
		Logger.log("Creating Wolfenstein...");

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		//environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		initShaders();

        cam = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(-31, 11, 27);
        cam.lookAt(0, 11, 0);
		cam.near = 1f;
		cam.far = 200f;
		cam.update();

		modelBatch = new ModelBatch();


		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		instance = loadScene();


	}

	private void initShaders() {
		shaderProgram = setupShader("scene");
		modelBatch = new ModelBatch(new DefaultShaderProvider()
		{
			@Override
			protected Shader createShader(final Renderable renderable)
			{
				return new SimpleTextureShader(renderable, shaderProgram);
			}
		});


		final Wolfenstein self = this;
		shaderProgramShadows = setupShader("shadows");
		modelBatchShadows = new ModelBatch(new DefaultShaderProvider()
		{
			@Override
			protected Shader createShader(final Renderable renderable)
			{
				return new ShadowMapShader(self, renderable, shaderProgramShadows);
			}
		});
	}

	private ModelInstance loadScene() {
		final G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
		model = loader.loadModel(Gdx.files.internal("models/scene_f0.g3db"));
		ModelInstance modelInstance = new ModelInstance(model);
		modelInstance.transform.setToScaling(4f, 4f, 4f);

		lights.add(new PointLight(new Vector3(0f, 13.8f, 32f)));
		lights.add(new PointLight(new Vector3(-25.5f, 12.0f, -26f)));
        lights.add(new DirectionalLight(new Vector3(33, 10, 3), new Vector3(-10, 0, 0)));
		lights.add(new MovingPointLight(new Vector3(0f,  15.0f, 0f)));

		return modelInstance;
	}

	@Override
	public void resize(int width, int height)
	{
		Logger.log("Window resized");
		cam.viewportHeight = height;
		cam.viewportWidth = width;
		cam.update();
	}

	public long lastScreenShot;
	public static boolean takeScreenshots = false;

	public void update() {

		if (System.currentTimeMillis() - lastScreenShot > 1000 * 1 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			Wolfenstein.takeScreenshots = true;
			// Force an update on all lights, else the render function won't be called and no screenshot taken
			for (final Light light : lights)
			{
				light.needsUpdate = true;
			}
			lastScreenShot = System.currentTimeMillis();
		}
		else
		{
			Wolfenstein.takeScreenshots = false;
		}

		for (final Light light : lights) {
			light.act(Gdx.graphics.getDeltaTime());
		}
		camController.update();
	}

	@Override
	public void render () {

		update();

		// Input ctrl should not be here!!
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for (final Light light : lights)
		{
			light.render(instance);
		}
		renderShadows();
		renderScene();

	}


	/**
	 * Render the main scene, final render
	 */
	public void renderScene()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		shaderProgram.begin();
		final int textureNum = 4;
		frameBufferShadows.getColorBufferTexture().bind(textureNum);
		shaderProgram.setUniformi("u_shadows", textureNum);
		shaderProgram.setUniformf("u_screenWidth", Gdx.graphics.getWidth());
		shaderProgram.setUniformf("u_screenHeight", Gdx.graphics.getHeight());
		shaderProgram.end();

		modelBatch.begin(cam);
		modelBatch.render(instance);
		modelBatch.end();

		if (takeScreenshots)
		{
			ScreenshotFactory.saveScreenshot(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), "scene");
		}


	}


	public void renderShadows()
	{
		if (frameBufferShadows == null)
		{
			frameBufferShadows = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		}
		frameBufferShadows.begin();

		Gdx.gl.glClearColor(1f, 1f, 1f, 0.4f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatchShadows.begin(cam);
		modelBatchShadows.render(instance);
		modelBatchShadows.end();
		frameBufferShadows.end();

        if (takeScreenshots)
        {
            ScreenshotFactory.saveScreenshot(frameBufferShadows.getWidth(), frameBufferShadows.getHeight(), "shadows");
        }
	}


	private ShaderProgram setupShader(String prefix) {

		final ShaderProgram shaderProgram = new ShaderProgram(
				Gdx.files.internal("shaders/" + prefix + "_v.glsl"),
				Gdx.files.internal("shaders/" + prefix + "_f.glsl"));

		if (!shaderProgram.isCompiled())
		{
			System.err.println("Error with shader " + prefix + ": " + shaderProgram.getLog());
			System.exit(1);
		}
		else
		{
			Logger.log("Shader " + prefix + " compilled " + shaderProgram.getLog());
		}
		return shaderProgram;
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
