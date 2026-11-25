package fr.azrotho;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public Main main;

    public InputHandler(Main main) {
        this.main = main;
    }

    public void keyTyped(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("Key pressed: " + keyEvent.getKeyCode());
        switch(keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                main.Barre1Y -= 10.0f;
                break;
            case KeyEvent.VK_LEFT:
                main.Barre1Y += 10.0f;
                break;
            case KeyEvent.VK_RIGHT:
                main.Barre2Y -= 10.0f;
                break;
            case KeyEvent.VK_DOWN:
                main.Barre2Y += 10.0f;
                break;
        }

        keyEvent.consume();
    }

    public void keyReleased(KeyEvent keyEvent) {
        keyEvent.consume();
    }
}
