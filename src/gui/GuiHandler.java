package gui;

import audio.AudioHandler;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Input;
import level.LevelHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GuiHandler {

    private static ArrayList<GuiTexture> mainMenuGUI;
    private static ArrayList<GuiTexture> pauseMenuGUI;
    private static ArrayList<GuiTexture> levelCompleteGUI;
    private static ArrayList<GuiTexture> levelSelectorGUI;
    private static ArrayList<GuiTexture> gameCompleteGUI;

    private static ArrayList<GuiTexture> toRender;
    private static ArrayList<GuiTexture> levelImages;

    private static GuiRenderer guiRenderer;

    private static GuiLinkedList mainMenuButtons;
    private static GuiLinkedList pauseMenuButtons;
    private static GuiLinkedList levelCompleteButtons;
    private static GuiLinkedList levelSelectorButtons;
    private static GuiLinkedList gameCompleteButtons;

    public static boolean shouldRenderGUI = true;

    public static final String MAIN = "./res/GUI/GuiData/mainMenuGuiData.txt";
    public static final String PAUSE = "./res/GUI/GuiData/pauseMenuGuiData.txt";
    public static final String LEVEL_COMPLETE = "./res/GUI/GuiData/levelCompleteGuiData.txt";
    public static final String LEVEL_SELECTOR = "./res/GUI/GuiData/levelSelectorGuiData.txt";
    public static final String GAME_COMPLETE = "./res/GUI/GuiData/gameCompleteGuiData.txt";

    public static String whichMenuToRender = "MAIN";
    
    public static void loadGUI(Loader loader) {

        mainMenuGUI = new ArrayList<>();
        pauseMenuGUI = new ArrayList<>();
        levelSelectorGUI = new ArrayList<>();
        levelCompleteGUI = new ArrayList<>();
        gameCompleteGUI = new ArrayList<>();

        levelImages = new ArrayList<>();

        guiRenderer = new GuiRenderer(loader);
        toRender = new ArrayList<>();

        mainMenuButtons = new GuiLinkedList();
        pauseMenuButtons = new GuiLinkedList();
        levelCompleteButtons = new GuiLinkedList();
        levelSelectorButtons = new GuiLinkedList();
        gameCompleteButtons = new GuiLinkedList();

        loadLevelImages("./res/GUI/GuiData/levelImagesData.txt",loader);

        loadGUI(MAIN,loader,mainMenuGUI,mainMenuButtons);
        loadGUI(PAUSE,loader,pauseMenuGUI,pauseMenuButtons);
        loadGUI(LEVEL_COMPLETE,loader,levelCompleteGUI,levelCompleteButtons);
        loadGUI(LEVEL_SELECTOR,loader,levelSelectorGUI,levelSelectorButtons);
        loadGUI(GAME_COMPLETE,loader,gameCompleteGUI,gameCompleteButtons);

    }

    private static void loadLevelImages(String path, Loader loader) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String data;
            while ((data = br.readLine()) != null) {
                String[] currentLine = data.split(" ");
                GuiTexture guiTexture = createGuiTexture(loader,currentLine);
                levelImages.add(guiTexture);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGUI(String path, Loader loader, ArrayList<GuiTexture> menu, GuiLinkedList buttons) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] data = currentLine.split(" ");
                GuiTexture gui = createGuiTexture(loader,data);
                if (data.length > 6) {
                    int[] values = {Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9])};
                    GuiNode node = new GuiNode(data[10],values,gui);
                    buttons.addGUI(node);
                } else {
                    menu.add(gui);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void render(Loader loader, Input input) {
        if (shouldRenderGUI && whichMenuToRender == "MAIN") {
            checkPressed(mainMenuButtons,loader,input);
            guiRenderer.render(mainMenuGUI);
            guiRenderer.render(toRender);
        } else if (shouldRenderGUI && whichMenuToRender == "PAUSE") {
            checkPressed(pauseMenuButtons,loader,input);
            guiRenderer.render(pauseMenuGUI);
            guiRenderer.render(toRender);
        } else if (shouldRenderGUI && whichMenuToRender == "LEVEL_COMPLETE") {
            checkPressed(levelCompleteButtons,loader,input);
            guiRenderer.render(levelCompleteGUI);
            guiRenderer.render(toRender);
        } else if (shouldRenderGUI && whichMenuToRender == "LEVEL_SELECTOR") {
            checkPressed(levelSelectorButtons,loader,input);
            toRender.add(levelImages.get(LevelHandler.currentLevel - 1));
            guiRenderer.render(levelSelectorGUI);
            guiRenderer.render(toRender);
        } else if (shouldRenderGUI && whichMenuToRender == "GAME_COMPLETE") {
            checkPressed(gameCompleteButtons,loader,input);
            guiRenderer.render(gameCompleteGUI);
            guiRenderer.render(toRender);
        }
    }

    private static GuiTexture createGuiTexture(Loader loader, String[] data) {
        GuiTexture gui = new GuiTexture(loader.loadTexture("/GUI/" + data[0]),new Vector2f(Float.parseFloat(data[1]),Float.parseFloat(data[2])),
                new Vector2f(Float.parseFloat(data[3]),Float.parseFloat(data[4])));
        return gui;
    }

    public static boolean isLevelFinished = false;

    public static void checkPressed(GuiLinkedList menuType, Loader loader, Input input) {
        float x = Mouse.getX();
        float y = Mouse.getY();

        //System.out.println(x + "   " + y);

        GuiNode current = menuType.first;

        for (int i=0;i<menuType.lenght;i++) {
            int[] data = current.bounds;
            if (x > data[0] && x < data[1] && y > data[2] && y < data[3]) {
                AudioHandler.playSound("UI_BUTTON_RELEASED");
                toRender.add(current.texture);
                if (input.isMouseLeftClick(0)) {
                    if (current.name.equals("PLAY")) {
                        System.out.println("PLAYING");
                        toRender.clear();
                        play();
                        AudioHandler.playSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
                    } else if (current.name.equals("EXIT")) {
                        System.out.println("EXITING");
                        exitGame();
                    } else if (current.name.equals("RESTART") || isLevelFinished || current.name.equals("NEXT_LEVEL") || current.name.equals("RESTART_GAME")) {
                        isLevelFinished = false;
                        AudioHandler.playSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
                        LevelHandler.load(loader);
                        loadGUI(loader);
                        play();
                    } else if (current.name.equals("RESUME")) {
                        AudioHandler.continuePlaying("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
                        System.out.println("RESUMING");
                        toRender.clear();
                        play();
                    } else if (current.name.equals("LEVEL")) {
                        System.out.println("Opening Level Selector");
                        toRender.clear();
                        whichMenuToRender = "LEVEL_SELECTOR";
                        LevelHandler.currentLevel = 1;
                    } else if (current.name.equals("NEXT")) {
                        toRender.clear();
                        LevelHandler.currentLevel = LevelHandler.currentLevel + 1;
                        if (LevelHandler.currentLevel > LevelHandler.numLevels) {
                            LevelHandler.currentLevel = 1;
                        }
                    } else if (current.name.equals("SELECT")) {
                        isLevelFinished = false;
                        AudioHandler.playSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
                        LevelHandler.load(loader);
                        loadGUI(loader);
                        play();
                    }
                }
                break;
            } else {
                toRender.clear();
            }
            current = current.next;
        }

    }

    public static void cleanUP() {
        guiRenderer.cleanUP();
    }

    public static void play() {
        LevelHandler.isPlayRequested = true;
        shouldRenderGUI = false;
    }

    public static void pause() {
        AudioHandler.pauseSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
        LevelHandler.isPlayRequested = false;
        whichMenuToRender = "PAUSE";
        shouldRenderGUI = true;
    }

    public static void levelComplete() {
        AudioHandler.pauseSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
        LevelHandler.isPlayRequested = false;
        whichMenuToRender = "LEVEL_COMPLETE";
        shouldRenderGUI = true;
    }

    public static void exitGame() {
        DisplayManager.setDisplayShouldClose(true);
    }

}
