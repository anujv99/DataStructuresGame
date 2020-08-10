package level;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.EnemyPlayerLoader;

import java.io.*;
import java.util.ArrayList;

public class LevelLoader {

    private ArrayList<Entity> collidors;
    private ArrayList<Entity> toRender;
    private ArrayList<EnemyPlayerLoader> enemies;

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

    public LevelLoader() {
        collidors = new ArrayList<>();
        toRender = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public void loadLevel(String fileName, Loader loader) {
        collidors.clear();
        toRender.clear();
        enemies.clear();
        readFile(fileName,loader);
    }

    public void renderLevel(Renderer renderer, StaticShader shader) {
        for (Entity entity:collidors) {
            renderer.render(entity,shader);
        }
        for (Entity entity:toRender) {
            renderer.render(entity,shader);
        }
    }

    private void readFile(String fileName, Loader loader) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("./res/levelData/"+ fileName + ".txt")));
            String data;
            int numEnemy = Integer.parseInt(br.readLine());
            String path = br.readLine();
            for (int i=1;i<=numEnemy;i++) {
                EnemyPlayerLoader enemyPlayerLoader = new EnemyPlayerLoader(loader,path + "_" + i);
                enemies.add(enemyPlayerLoader);
            }
            while ((data = br.readLine()) != null) {
                String[] values = data.split(" ");
                TexturedModel staticModel = createTexModel(loader, values);
                int numObjectcs = Integer.parseInt((values[1]));
                for (int i=0;i<numObjectcs*4;i+=4) {
                    float x = Float.parseFloat(values[i+4]);
                    float y =  Float.parseFloat(values[i+5]);
                    float z = Float.parseFloat(values[i+6]);
                    float scale = Float.parseFloat(values[i+7]);
                    Entity entity = new Entity(staticModel,new Vector3f(x,y,z),0,0,0,scale);
                    if (Integer.parseInt(values[3]) == 0) {
                        toRender.add(entity);
                    } else {
                        collidors.add(entity);
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TexturedModel createTexModel(Loader loader, String[] values) {
        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(values[2])));
        return staticModel;
    }

    public ArrayList<Entity> getCollidors() {
        return collidors;
    }

    public ArrayList<Entity> getToRender() {
        return toRender;
    }

    public ArrayList<EnemyPlayerLoader> getEnemies() {
        return enemies;
    }

}
