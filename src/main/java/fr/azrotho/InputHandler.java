package fr.azrotho;

import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;

import java.awt.event.KeyEvent;

public class InputHandler implements UserEventListener {

    public Main main;

    public InputHandler(Main main) {
        this.main = main;
    }

    public void userEventReceived(UserEvent userEvent) {
        if (userEvent == null || userEvent.getType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        System.out.println("Key pressed: " + userEvent.getCode());
        switch (userEvent.getCode()) {
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
    }
}
