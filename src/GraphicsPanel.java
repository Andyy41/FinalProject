import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private BufferedImage background;
    private BufferedImage block;
    private Timer timer;
    private Player player;
    // private Enemy enemy;
    private boolean[] pressedKeys;
    private double baseCd = 270;
    boolean rolled;
    boolean right;
    boolean left;
    boolean up;
    boolean down;
    int a = 0;
    String direction;
    private ArrayList<Enemy> enemies;
    private int wave;
    ArrayList<Point> spawnPoints = new ArrayList<>();
    double x;
    double y;// Track the current wave

    public GraphicsPanel() {
        timer = new Timer(5, this);
        timer.start();
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            block = ImageIO.read(new File("src/BLOCK.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player();
        enemies = new ArrayList<>();
        wave = 1;
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow();
        spawnEnemies(wave);
    }

    // Spawning enemies outside the map based on wave number
    private void spawnEnemies(int wave) {
        Random rand = new Random();
        int numberOfEnemies = wave * 5; // Increase enemies by 5 each wave

        for (int i = 0; i < numberOfEnemies; i++) {
            int edge = rand.nextInt(4);

            // Spawn at a random point on one of the edges
            if (edge == 0) { // Top edge
                x = rand.nextDouble() * getWidth();
                y = -50; // Outside the top of the screen
            } else if (edge == 1) { // Bottom edge
                x = rand.nextDouble() * getWidth();
                y = getHeight() + 50; // Outside the bottom of the screen
            } else if (edge == 2) { // Left edge
                x = -50; // Outside the left of the screen
                y = rand.nextDouble() * getHeight();
            } else { // Right edge
                x = getWidth() + 50; // Outside the right of the screen
                y = rand.nextDouble() * getHeight();
            }
            // Create a new Point to represent the spawn location
            Point spawnPoint = new Point((int) x, (int) y);

            // Check if this spawn point is too close to any other spawned enemy
            boolean tooClose = false;
            for (Point p : spawnPoints) {
                if (spawnPoint.distance(p) < 100) { // 100px threshold for distance
                    tooClose = true;
                    break;
                }
            }

            // If not too close to any other spawn, add to spawnPoints list
            if (!tooClose) {
                spawnPoints.add(spawnPoint);
                // Choose a random type of enemy (Common or Ranged)
                if (rand.nextBoolean()) {
                    enemies.add(new CommonEnemy(100, 10, 1, 0.5, spawnPoint.x, spawnPoint.y, "src/Enemy.png"));
                } else {
                    enemies.add(new RangedEnemy(80, 12, 1, 1.0, x, y, "src/RangedEnemy.png"));
                }
            }
        }
    }


    // Check if the player is moving (by checking movement keys)
    private boolean isMoving() {
        boolean x = pressedKeys[KeyEvent.VK_A] ||
                pressedKeys[KeyEvent.VK_D] ||
                pressedKeys[KeyEvent.VK_W] ||
                pressedKeys[KeyEvent.VK_S];
        //System.out.println(x);
        return x;
    }

    private boolean isDiagonalU() {
        boolean c = pressedKeys[KeyEvent.VK_W] && (pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_D]);
        //System.out.println("IS DU");
        return c;
    }

    private boolean isDiagonalD() {
        boolean r = pressedKeys[KeyEvent.VK_S] && (pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_D]);
        //System.out.println("IS DD");
        return r;
    }

    private boolean isUP() {
        boolean w = pressedKeys[KeyEvent.VK_W];
        //System.out.println("IS UP");
        return w;
    }

    private boolean isDown() {
        boolean s = pressedKeys[KeyEvent.VK_S];
        //System.out.println("IS DOWN");
        return s;
    }

    private boolean isRight() {
        boolean d = pressedKeys[KeyEvent.VK_D];
        //System.out.println("IS RIGHT");
        return d;
    }

    private boolean isLeft() {
        boolean a = pressedKeys[KeyEvent.VK_A];
        //System.out.println("IS LEFT");
        return a;
    }

    private boolean isRolled() {
        baseCd = 0;
        boolean roll = pressedKeys[KeyEvent.VK_SPACE];
        System.out.println("isRolled");
        return roll;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // the order that things get "painted" matter; we paint the background first
        g.drawImage(background, 0, 0, null);
        if (baseCd == 0 && isRolled()) {
           g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), isRolled(), isRight(), isLeft(), isUP(), isDown() , baseCd), (int) player.getxCoord(), (int) player.getyCoord(), null);
            if (baseCd > 0){
                baseCd--;
            }
        }
        g.drawImage(block, 50, 10, null);
        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.setColor(Color.red);
        g.drawString(Double.toString(baseCd), 50, 50);
        g.setFont(new Font("Times New Roman", Font.BOLD, 22));
        g.setColor(Color.white);
        g.drawString("Cooldowns", 50, 30);
        g.drawString("HP:" + player.GetHP(), 50, 60);
        g.setFont(new Font("Times New Roman", Font.BOLD, 22));
        g.setColor(Color.white);
        g.drawString("Wave: " + wave, 50, 80);
        player.updateFlashing();
        if (player.isInvincible()) {
            long elapsed = System.currentTimeMillis() - player.getInvincibleStartTime();
            if (elapsed >= player.getInvincibilityDuration()) {
                player.setInvincible(false);
            }
        }

        for (Enemy enemy : enemies) {
            // Move enemies towards the player
            enemy.moveTowardsPlayer(player);

            // Draw enemy
            g.drawImage(enemy.getCurrentImage(), (int) enemy.getX(), (int) enemy.getY(), null);
        }
        for (Enemy enemy : enemies) {
            // Only process collisions if the player is not invincible
            if (!player.isInvincible() && player.playerRect().intersects(enemy.getBoundingBox())) {
                player.Hit(enemy);
            }

            // Remove enemy if its HP is 0 or less
            if (enemy.getHp() <= 0 || player.playerRect().intersects(enemy.getBoundingBox())) {
                enemies.remove(enemy);  // Remove the enemy from the list
                break;  // Exit the loop as the list is modified during iteration
            }
        }


        checkWaveCompletion();
        if (player.isInvincible() && (System.currentTimeMillis() / 100) % 2 == 0) {
            g.drawImage(player.getHitIFRAME(), (int) player.getxCoord(), (int) player.getyCoord(), null);
        }

        if (player.isFlashing()) {
            g.drawImage(player.getHitIFRAME(), (int) player.getxCoord(), (int) player.getyCoord(), null);
        } else {
            // Draw the player as normal if not flashing
            g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), isRolled(), isRight(), isLeft(), isUP(), isDown(), baseCd), (int) player.getxCoord(), (int) player.getyCoord(), null);
        }

        if (player.isInvincible()) {
            long elapsed = System.currentTimeMillis() - player.getInvincibleStartTime();
            if (elapsed >= player.getInvincibilityDuration()) {
                player.setInvincible(false);
                player.updateFlashing();  // Reset flashing when invincibility ends
            }
        }


        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));


        // player moves left (A)
        if (pressedKeys[65]) {
            player.moveLeft();
        }


        // player moves right (D)
        if (pressedKeys[68]) {
            player.moveRight();
        }


        // player moves up (W)
        if (pressedKeys[87]) {
            player.moveUp();
        }


        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }
    }

    private void checkWaveCompletion() {
        // Check if all enemies are dead
        boolean allEnemiesDead = true;
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                allEnemiesDead = false;
                break;
            }
        }

        // If all enemies are dead, progress to the next wave
        if (allEnemiesDead) {
            wave++; // Increase wave
            enemies.clear(); // Clear current enemies
            spawnEnemies(wave); // Spawn new enemies for the next wave
        }
    }

    // ActionListener interface method
    @Override
    public void actionPerformed(ActionEvent e) {
        // repaints the window every 10ms
        repaint();
    }


    // KeyListener interface methods
    @Override
    public void keyTyped(KeyEvent e) { } // unimplemented


    @Override
    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }


    // MouseListener interface methods
    @Override
    public void mouseClicked(MouseEvent e) { }  // unimplemented because
    // if you move your mouse while clicking, this method isn't
    // called, so mouseReleased is best


    @Override
    public void mousePressed(MouseEvent e) { } // unimplemented


    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
        }
    }

    public double getCD(){
        return baseCd;
    }

    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented


    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented
}
