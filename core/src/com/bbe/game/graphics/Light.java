package com.bbe.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.bbe.game.Wolfenstein;
import com.bbe.game.utils.Logger;

public abstract class Light
{
    public static ShaderProgram	shaderProgram	= null;
    public static ModelBatch	modelBatch		= null;
    public Camera				camera;
    public Vector3				position		= new Vector3();
    public boolean				needsUpdate		= true;
    private Wolfenstein wolf;

    public abstract void act(float delta);

    public Light()
    {
        //this.wolf = wolf;
    }

    /**
     * Add the uniforms to the scene shader
     *
     * @param sceneShaderProgram
     */
    public abstract void applyToShader(ShaderProgram sceneShaderProgram);

    public void init()
    {
        if (shaderProgram == null)
        {
            shaderProgram = setupShader("depthmap");
            modelBatch = new ModelBatch(new DefaultShaderProvider()
            {
                @Override
                protected Shader createShader(final Renderable renderable)
                {
                    return new DepthMapShader(renderable, shaderProgram);
                }
            });
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

    /**
     * Create the depth map for this light
     *
     * @param modelInstance
     */
    public abstract void render(ModelInstance modelInstance);

}
