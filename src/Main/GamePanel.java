package Main;

import Entity.Player;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;  // 48x48 tile size
    public final int maxScreenCol = 16; //only 16 tiles can be displayed in a row of tiles
    public final int maxScreenRow = 12; //only 12 tiles can be displayed in a column of tiles
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler() ;
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    public void startGameThread() {
       gameThread = new Thread(this);
       gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (1000000000)/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;


        //create game loop inside here
        while (gameThread != null) {

            // 1 UPDATE: update information such as character positions
            update();

            //2 DRAW: draw the screen with the updated information
            repaint();

            try {
                double remainingTime = (nextDrawTime - System.nanoTime())/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void update() {

        player.update();

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        player.draw(g2);

        g2.dispose();
    }
}
