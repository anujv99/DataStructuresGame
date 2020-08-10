package toolbox;

import audio.AudioHandler;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import player.Movement;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.ArrayList;

public class BulletHandler {

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

    public ArrayList<Entity> bullets;
    public ArrayList<Entity> bulletsF;
    private Loader loader;
    private int index = 0;

    public BulletHandler(Loader loader) {
        this.loader = loader;
        bullets = new ArrayList<>();
        bulletsF = new ArrayList<>();
    }

    public void addBullet(Vector3f position) {
        if (index%50 == 0) {
            Entity entity = createBullet(position);
            if (Movement.shootDirection) {
                bullets.add(entity);
            } else {
                entity.increasePosition(-0.1f,0,0);
                bulletsF.add(entity);
            }
            AudioHandler.playSound("PLAYER_BULLET");
        }
    }

    private Entity createBullet(Vector3f position) {
        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("Misc/bullet"));
        TexturedModel texturedModel = new TexturedModel(model,modelTexture);
        Entity entity = new Entity(texturedModel, position, 0,0,0,0.01f);
        entity.increasePosition(0.05f,0.02f,0);
        return entity;
    }

    public void render(Renderer renderer, StaticShader shader) {
        index++;
        if (index == 150) {
            index = 0;
        }
        for (int i=0;i<bullets.size();i++) {
            Entity entity = bullets.get(i);
            if (entity.getPosition().x > 2 || entity.getPosition().x < -2) {
                bullets.remove(i);
            }
            renderer.render(entity, shader);
            entity.increasePosition(0.005f, Movement.getVelocity().y, Movement.getVelocity().z);
        }
        for (int i=0;i<bulletsF.size();i++) {
            Entity entity = bulletsF.get(i);
            if (entity.getPosition().x > 2 || entity.getPosition().x < -2) {
                bulletsF.remove(i);
            }
            renderer.render(entity, shader);
            entity.increasePosition(-0.005f, Movement.getVelocity().y, Movement.getVelocity().z);
        }
    }

}
