package player;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.*;
import java.util.ArrayList;

public class Player {

    private ArrayList<Entity> idle;
    private ArrayList<Entity> run;
    private ArrayList<Entity> runReverse;
    private ArrayList<Entity> jump;
    private ArrayList<Entity> jumpReverse;
    private ArrayList<Entity> shoot;
    private ArrayList<Entity> shootReverse;
    private ArrayList<Entity> die;

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

    public Player(Loader loader, String fileName) {
        idle = new ArrayList<>();
        run = new ArrayList<>();
        runReverse = new ArrayList<>();
        jump = new ArrayList<>();
        jumpReverse = new ArrayList<>();
        shoot = new ArrayList<>();
        shootReverse = new ArrayList<>();
        die = new ArrayList<>();
        loadPlayer(fileName,loader);
    }

    private void loadPlayer (String fileName, Loader loader) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("./res/PlayerData/" + fileName + ".txt")));
            String data;
            while ((data = br.readLine()) != null) {
                String[] values = data.split(" ");
                int numOfframes = Integer.parseInt(values[2]);
                for (int i=1;i<=numOfframes;i++) {
                    Entity entity = createEntity(values[1] + "(" + i + ")",loader);
                    if (values.length == 4) {
                        entity.setRotY(180);
                        entity.setPosition(new Vector3f(-0.065f,-0.10f/2,-1.5f));
                        if (values[1].contains("Run")) {
                            runReverse.add(entity);
                        } else if (values[1].contains("Jump")) {
                            jumpReverse.add(entity);
                        } else if (values[1].contains("Shoot")) {
                            shootReverse.add(entity);
                        }
                    } else {
                        if (values[1].contains("Idle")) {
                            idle.add(entity);
                        } else if (values[1].contains("Run")) {
                            run.add(entity);
                        } else if (values[1].contains("Jump")) {
                            jump.add(entity);
                        } else if (values[1].contains("Shoot")) {
                            shoot.add(entity);
                        } else if (values[1].contains("Die")) {
                            die.add(entity);
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

    private Entity createEntity (String path, Loader loader) {
        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture(path));
        TexturedModel texturedModel = new TexturedModel(model,modelTexture);
        Entity entity = new Entity(texturedModel,new Vector3f(0.065f,-0.10f/2,-1.5f),0,0,0,0.15f);
        return entity;
    }

    public int index = 0;
    private String currentMovement = null;

    public void idle (Renderer renderer, StaticShader shader) {
        if (currentMovement != "Idle") {
            index = 0;
            currentMovement = "Idle";
        }
        renderer.render(idle.get(index/20),shader);
        index++;
        if (index >= 200) {
            index = 0;
        }
    }

    public void die (Renderer renderer, StaticShader shader) {
        if (currentMovement != "Die") {
            index = 0;
            currentMovement = "Die";
        }
        renderer.render(die.get(index/40),shader);
        index++;
        if (index >= 400) {
            index = 0;
        }
    }

    public void shoot(Renderer renderer, StaticShader shader)  {
        if (currentMovement != "Shoot") {
            index = 0;
            currentMovement = "Shoot";
        }
        renderer.render(shoot.get(index/20),shader);
        index++;
        if (index >= 60) {
            index = 0;
        }
    }

    public void shootReverse(Renderer renderer, StaticShader shader)  {
        if (currentMovement != "ShootRev") {
            index = 0;
            currentMovement = "ShootRev";
        }
        renderer.render(shootReverse.get(index/20),shader);
        index++;
        if (index >= 60) {
            index = 0;
        }
    }

    public void run (Renderer renderer, StaticShader shader) {
        if (currentMovement != "Run") {
            index = 0;
            currentMovement = "Run";
        }
        renderer.render(run.get(index / 20), shader);
        index++;
        if (index >= 160) {
            index = 0;
        }
    }

    public void runRevere(Renderer renderer, StaticShader shader) {
        if (currentMovement != "RunReverse") {
            index = 0;
            currentMovement = "RunReverse";
        }
        renderer.render(runReverse.get(index / 20), shader);
        index++;
        if (index >= 160) {
            index = 0;
        }
    }
    public void jump (Renderer renderer, StaticShader shader) {
        if (currentMovement != "Jump") {
            index = 0;
            currentMovement = "Jump";
        }
        renderer.render(jump.get(index/20),shader);
        index++;
        if (index >= 200) {
            index = 0;
        }
    }
    public void jumpReverse (Renderer renderer, StaticShader shader) {
        if (currentMovement != "RevJump") {
            index = 0;
            currentMovement = "RevJump";
        }
        renderer.render(jumpReverse.get(index/20),shader);
        index++;
        if (index >= 200) {
            index = 0;
        }
    }

}
