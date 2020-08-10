package gui;

public class GuiLinkedList {

    public GuiNode first;
    public int lenght;

    public GuiLinkedList() {
        first = null;
        lenght = 0;
    }

    public void addGUI(GuiNode node) {
        lenght++;
        if (first == null) {
            first = node;
        } else {
            node.next = first;
            first = node;
        }
    }

}
