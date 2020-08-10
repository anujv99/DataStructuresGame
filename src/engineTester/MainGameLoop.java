package engineTester;

import audio.AudioHandler;
import gui.GuiHandler;

import level.LevelHandler;
import org.lwjgl.input.Keyboard;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolbox.*;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		AudioHandler.loadSounds();

		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		LevelHandler.load(loader);

		GuiHandler.loadGUI(loader);

		Input input = new Input();

		while(!DisplayManager.isCloseRequested()){
			if (Keyboard.isKeyDown(Keyboard.KEY_F10)) {
				AudioHandler.changeVolume(0.005f);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
				AudioHandler.changeVolume(-0.005f);
			}
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(LevelHandler.getCamera());
			LevelHandler.check(renderer,shader);
			//levelLoader.renderLevel(renderer,shader);
			//Movement.move(levelLoader,camera, player,renderer,shader);
			GuiHandler.render(loader, input);
			input.update();
			shader.stop();
			DisplayManager.updateDisplay();
		}

		AudioHandler.deleteEverything();
		GuiHandler.cleanUP();
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
