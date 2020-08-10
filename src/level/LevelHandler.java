package level;

import audio.AudioHandler;
import entities.Camera;
import gui.GuiHandler;
import org.lwjgl.input.Keyboard;
import player.Movement;
import player.Player;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;

public class LevelHandler {

    public static final int numLevels = 2;

    public static boolean isPlayRequested = false;
    public static LevelLoader levelLoader;
    private static Player player;
    private static Camera camera;
    public static int currentLevel = 1;

    public static boolean isSoundPlaying = false;

    public static void check(Renderer renderer, StaticShader shader) {
        if (levelLoader.getEnemies().size() == 0 && isPlayRequested) {
            GuiHandler.isLevelFinished = true;
            GuiHandler.levelComplete();
            currentLevel++;
            if (currentLevel > numLevels) {
                GuiHandler.whichMenuToRender = "GAME_COMPLETE";
                currentLevel = 1;
            }
            System.out.println("Loading Next Level: " + currentLevel);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && isPlayRequested) {
            GuiHandler.pause();
        }
        if (isPlayRequested) {
            levelLoader.renderLevel(renderer,shader);
            Movement.move(levelLoader,camera, player,renderer,shader);
            AudioHandler.stopSound("UI_MENU");
            isSoundPlaying = false;
        } else if (!isSoundPlaying){
            AudioHandler.setVolume("UI_MENU", 0.4f);
            AudioHandler.playSound("UI_MENU");
            isSoundPlaying = true;
        }
    }

    public static void load(Loader loader) {
        System.out.println("Level Loaded: " + currentLevel);
        loader.cleanUp();
        levelLoader = new LevelLoader();
        levelLoader.loadLevel("level" + currentLevel,loader);
        player = new Player(loader,"PlayerOne/PlayerOne");
        camera = null;
        camera = new Camera();
        Movement.create(loader);
    }

    public static Camera getCamera() {
        return camera;
    }

}
