package fr.azrotho;

import org.dvb.event.EventManager;
import org.dvb.event.UserEventRepository;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import java.awt.*;

public class Main extends Container implements Xlet, Runnable {

    private HScene scene;
    private Font font;
    private Image image;
    private InputHandler inputHandler;
    private Thread gameThread;
    private volatile boolean running = false;

    private int SIZE_BARRE = 150;
    private int BARRE_WIDTH = 5;

    public float Barre1X = 10.0f;
    public volatile float Barre1Y = 720.0f/2.0f - SIZE_BARRE/2.0f;

    public float Barre2X = 1280.0f - 10.0f-5;
    public volatile float Barre2Y = 720.0f/2.0f - SIZE_BARRE/2.0f;

    private float BalleX = 1280.0f/2.0f;
    private float BalleY = 720.0f/2.0f;
    private float BalleDirX = 1.0f;
    private float BalleDirY = 1.0f;
    private float BalleSpeed = 2.0f;


    public int score1 = 0;
    public int score2 = 0;

    public void initXlet(XletContext context) {
        setSize(1280, 720);
        try {
            image = AssetLoader.loadImage("img/kekw.png");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        scene = HSceneFactory.getInstance().getDefaultHScene();
        scene.add(this);
        inputHandler = new InputHandler(this);
        UserEventRepository repo = new UserEventRepository("PongInput");
        repo.addAllArrowKeys();
        EventManager.getInstance().addUserEventListener(inputHandler, repo);
        scene.validate();
    }

    public void startXlet() {
        setVisible(true);
        scene.setVisible(true);
        this.requestFocus();

        if (!running) {
            running = true;
            gameThread = new Thread(this, "PongGameLoop");
            gameThread.start();
        }
    }

    public void run() {
        final long frameDurationMs = 16L;
        while (running) {
            long frameStart = System.currentTimeMillis();

            // UPDATE
            BalleX += BalleDirX * BalleSpeed;
            BalleY += BalleDirY * BalleSpeed;

            if(BalleY <= 0.0f || BalleY >= 720.0f) {
                BalleDirY *= -1.0f;
            }

            if(BalleX <= 0.0f) {
                score2++;
                BalleX = 1280.0f/2.0f;
                BalleY = 720.0f/2.0f;
                BalleDirX *= -1.0f;
            }
            if(BalleX >= 1280.0f) {
                score1++;
                BalleX = 1280.0f/2.0f;
                BalleY = 720.0f/2.0f;
                BalleDirX *= -1.0f;
            }

            if(BalleX <= Barre1X + BARRE_WIDTH && BalleY >= Barre1Y && BalleY <= Barre1Y + SIZE_BARRE) {
                BalleDirX *= -1.0f;
            }

            if(BalleX >= Barre2X && BalleY >= Barre2Y && BalleY <= Barre2Y + SIZE_BARRE) {
                BalleDirX *= -1.0f;
            }

            // Render
            repaint();

            long elapsed = System.currentTimeMillis() - frameStart;
            long sleepMs = frameDurationMs - elapsed;
            if (sleepMs > 0L) {
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private void stopGameLoop() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    public void pauseXlet() {
        stopGameLoop();
        setVisible(false);
    }

    public void destroyXlet(boolean unconditional) {
        stopGameLoop();
        if (inputHandler != null) {
            EventManager.getInstance().removeUserEventListener(inputHandler);
            inputHandler = null;
        }
        AssetLoader.clearImageCache();
        image = null;
        if (scene != null) {
            scene.remove(this);
            scene = null;
        }
    }

    public void paint(Graphics g) {
        g.setColor(new Color(0x000000));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Barre 1
        g.setColor(new Color(0xffffff));
        g.fillRect((int)Barre1X, (int)Barre1Y, BARRE_WIDTH, SIZE_BARRE);
        // Barre 2
        g.fillRect((int)Barre2X, (int)Barre2Y, BARRE_WIDTH, SIZE_BARRE);
        // Balle
        g.fillRect((int)BalleX, (int)BalleY, 10, 10);



        g.setColor(new Color(0xffffff));
        if (font == null) {
            font = new Font(null, Font.PLAIN, 30);
        }
        g.setFont(font);
        g.drawString(String.valueOf(score1), 50, 100);
        g.drawString(String.valueOf(score2), 1000, 100);
    }
}
