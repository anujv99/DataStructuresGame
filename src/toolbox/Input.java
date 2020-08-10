package toolbox;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {

    private boolean keys[];
    private boolean mouseLeftClick;

    public Input() {
        this.keys = new boolean[Keyboard.KEYBOARD_SIZE];
        for (int i=0;i<Keyboard.KEYBOARD_SIZE;i++) {
            keys[i] = false;
        }
        mouseLeftClick = false;
    }

    public boolean isKeyDown(int key) {
        return Keyboard.isKeyDown(key);
    }

    public boolean isMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    public void update() {
        mouseLeftClick = isMouseButtonDown(0);
    }

    public boolean isMouseLeftClick(int button) {
        return (isMouseButtonDown(button) && !mouseLeftClick);
    }

    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }

}
