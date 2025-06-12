import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import javax.swing.JButton;


public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private BufferedImage background;
    private BufferedImage block;
    private BufferedImage Black;
    private Timer timer;
    private Player player;
    private boolean[] pressedKeys;
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
    private boolean canRoll;
    private final long ROLL_COOLDOWN = 1000; // 1 second
    private long lastRollTime = 0; // when roll started
    private ArrayList<PlayerProjectile> projectiles = new ArrayList<>();
    private String bulletImage;
    private ArrayList<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private String enemyBullet;
    private JButton Start;
    private boolean begin;
    private boolean waveCleared = false;
    private long waveClearTime = 0;
    private final long WAVE_DELAY = 2000; // 2-second delay between waves
    private MusicPlayer musicPlayer;


    public GraphicsPanel() {
        // Initialize your music player and load the music file
        musicPlayer = new MusicPlayer("src/Music.wav");
        try {
            background = ImageIO.read(new File("src/Room.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            block = ImageIO.read(new File("src/BLOCK.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            Black = ImageIO.read(new File("src/Black.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        Start = new JButton("Play");
        player = new Player();
        enemies = new ArrayList<>();
        wave = 1;
        begin = false;
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow();
        spawnEnemies(wave);
        timer = new Timer(10, this);
        timer.start();
        add(Start);
    }

    private void spawnEnemies(int wave) {
        // Common enemies: Increase by 2 more each wave (1, 3, 5, 7...)
        int numberOfCommonEnemies = (wave * 2) - 1;  // Ensures 1, 3, 5, 7, etc.

        // Ranged enemies: Every 3 waves, spawn 2 more
        int numberOfRangedEnemies = 0;
        if (wave >= 3) {
            numberOfRangedEnemies = 2 * (wave / 3);  // Every 3 waves, add 2 more ranged enemies
        }

        // Spawn common enemies
        for (int i = 0; i < numberOfCommonEnemies; i++) {
            // Generate a random position for each common enemy
            double x = getRandomX();
            double y = getRandomY();
            enemies.add(new CommonEnemy(100, 10, 2, 1.0, x, y, "src/Enemy.png")); // Spawn a common enemy
        }

        // Spawn ranged enemies (starting from wave 3)
        for (int i = 0; i < numberOfRangedEnemies; i++) {
            // Generate a random position for each ranged enemy
            double x = getRandomX();
            double y = getRandomY();
            enemies.add(new RangedEnemy(70, 20, 1, 1.0, x, y, "src/RangedEnemy.png"));
        }
    }

    private double getRandomX() {
        Random rand = new Random();
        int edge = rand.nextInt(4);  // Randomly choose one of the four edges

        switch (edge) {
            case 0: // Top edge
            case 1: // Bottom edge
                return rand.nextDouble() * (988 - 50) + 25;  // Spawn between 25 and (width - 25)
            case 2: // Left edge
                return -25;  // Spawn exactly 25 units outside the left edge
            case 3: // Right edge
                return 988 + 25;  // Spawn exactly 25 units outside the right edge
            default:
                return 0;  // Default (should never hit this)
        }
    }

    private double getRandomY() {
        Random rand = new Random();
        int edge = rand.nextInt(4);  // Randomly choose one of the four edges

        switch (edge) {
            case 0: // Top edge
                return -25;  // Spawn exactly 25 units above the top edge
            case 1: // Bottom edge
                return 540 + 25;  // Spawn exactly 25 units below the bottom edge
            case 2: // Left edge
            case 3: // Right edge
                return rand.nextDouble() * (540 - 50) + 25;  // Spawn between 25 and (height - 25)
            default:
                return 0;  // Default (should never hit this)
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
        boolean roll = pressedKeys[KeyEvent.VK_SPACE] && canRoll;
        if (roll) {
            canRoll = false; // prevent rolling until cooldown is done
        }
        return roll;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.update();
        // the order that things get "painted" matter; we paint the background first

        // Update cooldown
        long now = System.currentTimeMillis();
        long cooldownRemaining = Math.max(0, ROLL_COOLDOWN - (now - lastRollTime));

        if (!begin) {
            g.drawImage(Black, 0, 0, null);
            Start.addActionListener(this);
            Start.setLocation(425, 200);
        } else{
           musicPlayer.playMusic();
        }

        // Draw player based on state
        if (!(player.getHP() <= 0) && begin) {
            remove(Start);
            g.drawImage(background, 0, 0, null);
            g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), isRight(), isLeft(), isUP(), isDown()), (int) player.getxCoord(), (int) player.getyCoord(), null);
            g.drawImage(block, 50, 10, null);
            g.setFont(new Font("Arial", Font.ITALIC, 14));
            g.setColor(Color.red);
            g.setFont(new Font("Times New Roman", Font.BOLD, 22));
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.ITALIC, 14));
            g.setColor(Color.red);
            g.drawString("Roll CD: " + (cooldownRemaining / 1000.0) + "s", 65, 30);
            g.drawString("HP:" + player.GetHP(), 60, 50);
            g.setFont(new Font("Times New Roman", Font.BOLD, 22));
            g.setColor(Color.white);
            g.drawString("Wave: " + wave, 70, 75);
            player.updateFlashing();
            Iterator<Enemy> iterator = enemies.iterator();
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();
                if (enemy.getHp() <= 0) {
                    iterator.remove();  // Safe removal
                }
            }

            if (player.isInvincible()) {
                long elapsed = System.currentTimeMillis() - player.getInvincibleStartTime();
                if (elapsed >= player.getInvincibilityDuration()) {
                    player.setInvincible(false);
                }
            }

            for (Enemy enemy : enemies) {
                // Draw enemy
                g.drawImage(enemy.getCurrentImage(), (int) enemy.getX(), (int) enemy.getY(), null);
                // Update and draw enemy projectiles
                for (int i = 0; i < enemyProjectiles.size(); i++) {
                    EnemyProjectile ep = enemyProjectiles.get(i);
                    ep.update();
                    ep.draw(g);
                    if (ep.isOffScreen(getWidth(), getHeight())) {
                        enemyProjectiles.remove(i);
                        i--;
                    } else if (player.playerRect().intersects(ep.getBounds()) && !player.isInvincible()) {
                        player.Hit(enemy);  // Or some damage logic
                        enemyProjectiles.remove(i);
                        i--;
                    }
                }


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


            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);

                // Check collisions with projectiles
                for (int j = 0; j < projectiles.size(); j++) {
                    PlayerProjectile p = projectiles.get(j);
                    if (enemy.getBoundingBox().intersects(p.getBounds())) {
                        enemy.decreaseHp(10); // or however much
                        projectiles.remove(j);
                        j--;
                        break;
                    }
                }

                // Remove dead enemies
                if (enemy.getHp() <= 0) {
                    enemies.remove(i);
                    i--;
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
                g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), isRight(), isLeft(), isUP(), isDown()), (int) player.getxCoord(), (int) player.getyCoord(), null);
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

            if (pressedKeys[32]) {
                rolled = true;
            }
            // Update and draw projectiles
            for (int i = 0; i < projectiles.size(); i++) {
                PlayerProjectile p = projectiles.get(i);
                p.update();
                p.draw(g);
                if (p.isOffScreen(getWidth(), getHeight())) {
                    projectiles.remove(i);
                    i--;
                }
            }
        }
        if (player.getHP() <= 0) {
            g.drawImage(Black, 0, 0, null);
            g.setColor(Color.RED);
            Font font = new Font("Arial", Font.BOLD, 30);
            g.setFont(font);
            g.drawString("YOU LOSE", 375, 200);
            g.drawString("Wave Reached: " + wave, 350, 300);
        }
    }

    private void checkWaveCompletion() {
        if (!waveCleared && enemies.isEmpty()) {
            waveCleared = true;
            waveClearTime = System.currentTimeMillis();
        }

        if (waveCleared) {
            long now = System.currentTimeMillis();
            if (now - waveClearTime >= WAVE_DELAY) {
                wave++; // Next wave
                enemies.clear();
                spawnPoints.clear(); // Reset spawn points to avoid proximity issues
                spawnEnemies(wave);
                waveCleared = false;
            }
        }
    }


    // ActionListener interface method
    @Override
        public void actionPerformed(ActionEvent e) {
        Object sender = e.getSource();
        if (sender == Start) {
            begin = true;  // Start the game when the button is clicked
            musicPlayer.playMusic(); // Start playing music immediately when the game begins
        }
        if (begin) {
            for (Enemy enemy : enemies) {
                enemy.moveTowardsPlayer(player);
                if (enemy instanceof RangedEnemy ranged) {
                    if (ranged.canShoot()) {
                        // Shoot only once if cooldown is valid
                        enemyProjectiles.add(ranged.shootAt(player, "src\\EnemyBullet.png"));
                    }
                }
            }

            // Update enemy projectiles (same)
            for (int i = 0; i < enemyProjectiles.size(); i++) {
                EnemyProjectile ep = enemyProjectiles.get(i);
                ep.update();
                if (ep.isOffScreen(getWidth(), getHeight())) {
                    enemyProjectiles.remove(i);
                    i--;
                } else if (player.playerRect().intersects(ep.getBounds()) && !player.isInvincible()) {
                    player.Hit(null);  // Player gets hit by the projectile
                    enemyProjectiles.remove(i);
                    i--;
                }
            }

            repaint();  // Ensure graphics are updated
        }
    }




    // KeyListener interface methods
    @Override
    public void keyTyped(KeyEvent e) {
    } // unimplemented


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;

        if (key == KeyEvent.VK_SPACE && !player.isRolling()) {
            long now = System.currentTimeMillis();
            if (now - lastRollTime >= ROLL_COOLDOWN) {
                double dx = 0;
                double dy = 0;
                if (pressedKeys[KeyEvent.VK_W]) dy -= 5;
                if (pressedKeys[KeyEvent.VK_S]) dy += 5;
                if (pressedKeys[KeyEvent.VK_A]) dx -= 5;
                if (pressedKeys[KeyEvent.VK_D]) dx += 5;

                if (dx != 0 || dy != 0) {
                    lastRollTime = now;
                    player.startRoll(dx, dy);
                }
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }


    // MouseListener interface methods
    @Override
    public void mouseClicked(MouseEvent e) {
    }  // unimplemented because
    // if you move your mouse while clicking, this method isn't
    // called, so mouseReleased is best


    @Override
    public void mousePressed(MouseEvent e) {
    } // unimplemented


    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // Left click
            double playerX = player.getxCoord();
            double playerY = player.getyCoord();
            double targetX = e.getX();
            double targetY = e.getY();

            // Center projectile on player
            PlayerProjectile p = new PlayerProjectile(playerX + 16, playerY + 16, targetX, targetY, 5, "src\\PlayerBullet.png");
            projectiles.add(p);
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented


    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented


}
