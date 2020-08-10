package player;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.ArrayList;

public class Health {

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

    public int health = 10;
    public Entity text;
    public ArrayList<Entity> toRender;

    public Health(Loader loader, Vector3f position) {
        TexturedModel texturedModel = new TexturedModel(loader.loadToVAO(vertices,textureCoords,indices), new ModelTexture(loader.loadTexture("/Misc/power")));
        TexturedModel texturedModel1 = new TexturedModel(loader.loadToVAO(vertices,textureCoords,indices), new ModelTexture(loader.loadTexture("/Misc/healthtext")));
        text = new Entity(texturedModel1,position,0,0,0,0.08f);
        toRender = new ArrayList<>();
        float x = 0;
        for (int i=0;i<10;i++) {
            Entity entity = new Entity(texturedModel, new Vector3f(position.x + 0.1f + x,position.y,position.z),0,0,0,0.008f);
            toRender.add(entity);
            x += 0.01f;
        }
    }

    public void render(Renderer renderer, StaticShader shader) {
        renderer.render(text,shader);
        for (int i=0;i<health;i++) {
            Entity entity = toRender.get(i);
            renderer.render(entity,shader);
        }
    }
}
