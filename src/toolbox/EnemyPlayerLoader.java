package toolbox;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import player.Movement;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.*;
import java.util.ArrayList;

public class EnemyPlayerLoader {

    public ArrayList<Entity> die;
    public ArrayList<Entity> run;
    private ArrayList<Entity> runReverse;
    private ArrayList<Entity> dieRev;
    public int health;

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

    public EnemyPlayerLoader (Loader loader, String fileName) {
        health = 7;
        die = new ArrayList<>();
        run = new ArrayList<>();
        runReverse = new ArrayList<>();
        dieRev = new ArrayList<>();
        loadPlayer(fileName,loader);
    }

    public void render(Renderer renderer, StaticShader shader) {
        sync();
        if (health <= 0) {
            if (speed > 0) {
                speed = 0;
                die(renderer,shader);
            } else {
                speed = 0;
                dieRev(renderer,shader);
            }
        } else {
            run(renderer, shader);
        }
    }

    private void loadPlayer (String fileName, Loader loader) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("./res/" + fileName + ".txt")));
            String data;
            String[] positions = br.readLine().split(" ");
            boundX = Float.parseFloat(positions[4]);
            boundY = Float.parseFloat(positions[5]);
            while ((data = br.readLine()) != null) {
                String[] values = data.split(" ");
                int numOfframes = Integer.parseInt(values[2]);
                for (int i=1;i<=numOfframes;i++) {
                    Entity entity = createEntity(values[1] + "(" + i + ")",loader, positions);
                    if (values.length == 4) {
                        entity.setRotY(180);
                        if (values[1].contains("Run")) {
                            runReverse.add(entity);
                        } else if (values[1].contains("Die")){
                            entity.setScale(0.16f);
                            entity.increasePosition(0,-0.02f,0);
                            dieRev.add(entity);
                        }
                    } else {
                        if (values[1].contains("Die")) {
                            entity.setScale(0.16f);
                            entity.increasePosition(0,-0.02f,0);
                            die.add(entity);
                        } else if (values[1].contains("Run")) {
                            run.add(entity);
                        }
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

    private Entity createEntity (String path, Loader loader, String[] positions) {
        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture(path));
        TexturedModel texturedModel = new TexturedModel(model,modelTexture);
        Entity entity = new Entity(texturedModel,new Vector3f(Float.parseFloat(positions[0]),Float.parseFloat(positions[1]),Float.parseFloat(positions[2])),0,0,0,Float.parseFloat(positions[3]));
        return entity;
    }

    private void sync() {
        for (int i=0;i<run.size();i++) {
            run.get(i).increasePosition(Movement.getVelocity().x, Movement.getVelocity().y-0.00005f, Movement.getVelocity().z);
            runReverse.get(i).increasePosition(Movement.getVelocity().x, Movement.getVelocity().y-0.00005f, Movement.getVelocity().z);
            run.get(i).increasePosition(speed,0,0);
            runReverse.get(i).increasePosition(speed,0,0);
        }
        for (int i=0;i<die.size();i++) {
            die.get(i).increasePosition(Movement.getVelocity().x,Movement.getVelocity().y - 0.00005f,0);
            die.get(i).increasePosition(speed,0,0);
            dieRev.get(i).increasePosition(Movement.getVelocity().x,Movement.getVelocity().y - 0.00005f,0);
            dieRev.get(i).increasePosition(speed,0,0);
        }
    }

    public int index = 0;
    private String currentMovement = null;
    float boundX, boundY;
    float speed = 0.001f;
    public boolean shouldRemove = false;

    public void die(Renderer renderer, StaticShader shader) {
        if (currentMovement != "Die") {
            index = 0;
            currentMovement = "Die";
        }
        renderer.render(die.get(index/40),shader);
        index++;
        if (index >= 480) {
            shouldRemove = true;
            index = 0;
        }
    }

    public void dieRev(Renderer renderer, StaticShader shader) {
        if (currentMovement != "DieRev") {
            index = 0;
            currentMovement = "DieRev";
        }
        renderer.render(dieRev.get(index/40),shader);
        index++;
        if (index >= 480) {
            shouldRemove = true;
            index = 0;
        }
    }

    public void run (Renderer renderer, StaticShader shader) {
        if (currentMovement != "Run") {
            index = 0;
            currentMovement = "Run";
        }
        if (speed > 0) {
            renderer.render(run.get(index / 20), shader);
        } else {
            renderer.render(runReverse.get(index / 20),shader);
        }
        boundX += Movement.getVelocity().x;
        boundY += Movement.getVelocity().x;
        if (run.get(0).getPosition().x > boundX && speed > 0) {
            speed = -speed;
        } else if (run.get(0).getPosition().x < boundY && speed < 0) {
            speed = -speed;
        }
        index++;
        if (index >= 200) {
            index = 0;
        }
    }
}
