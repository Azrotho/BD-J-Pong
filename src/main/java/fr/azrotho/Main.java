package fr.azrotho;

import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import java.awt.*;

public class Main extends Container implements Xlet {

    private HScene scene;
    private Font font;
    private Image image;

    private int SIZE_BARRE = 50;
    private int BARRE_WIDTH = 5;

    public float Barre1X = 10.0f;
    public float Barre1Y = 720.0f/2.0f - SIZE_BARRE/2.0f;

    public float Barre2X = 1280.0f - 10.0f-5;
    public float Barre2Y = 720.0f/2.0f - SIZE_BARRE/2.0f;

    private float BalleX = 1280.0f/2.0f;
    private float BalleY = 720.0f/2.0f;
    private float BalleDirX = 1.0f;
    private float BalleDirY = 1.0f;
    private float BalleSpeed = 0.1f;


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
        scene.addKeyListener(new InputHandler(this));
        scene.validate();
    }

    public void startXlet() {
        setVisible(true);
        scene.setVisible(true);
        this.requestFocus();


        float milis = 0.0f;
        long lastTime = System.currentTimeMillis();
        while(true) {
            if(milis >= (1.0f / 60.0f)) {
                milis = 0.0f;


                // INPUT



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

                //x++;
                // Render
                repaint();
                lastTime = System.currentTimeMillis();
            }
            milis += (System.currentTimeMillis() - lastTime) / 1000.0f;

        }
    }

    public void pauseXlet() {
        setVisible(false);
    }

    public void destroyXlet(boolean unconditional) {
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
