package gui;

public class GuiNode {

    public String name;
    public int[] bounds;
    public GuiTexture texture;
    public GuiNode next;

    public GuiNode(String name, int[] bounds, GuiTexture texture) {
        this.name = name;
        this.bounds = bounds;
        this.texture = texture;
        this.next = null;
    }

}
