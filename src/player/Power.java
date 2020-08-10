package player;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.ArrayList;

public class Power {

    private final float[] vertices= {
            -1,1,0,
            -1,-1,0,
            1,-1,0,
            1,1,0
    };

    private final float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
    };

    private final int[] indices = {
            0,1,2,
            0,2,3
    };

    public int totalPower = 5;
    public ArrayList<Entity> toRender;
    public Entity text;

    public Power(Loader loader) {
        TexturedModel texturedModel = new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture("/Misc/power")));
        TexturedModel texturedModel1 = new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture("/Misc/powertext")));
        text = new Entity(texturedModel1,new Vector3f(0.7f,0.499f,-1.5f),0,0,0,0.08f);
        toRender = new ArrayList<>();
        float x = 0;
        for (int i=0;i<5;i++) {
            Entity entity = new Entity(texturedModel, new Vector3f(0.8f + x,0.5f,-1.5f),0,0,0,0.008f);
            toRender.add(entity);
            x += 0.02f;
        }
    }

    public void render(Renderer renderer, StaticShader shader) {
        renderer.render(text,shader);
        for (int i=0;i<totalPower;i++) {
            Entity entity = toRender.get(i);
            renderer.render(entity,shader);
        }
    }

}
