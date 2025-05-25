package src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements KeyListener {
    private final int MOVE_AMT = 3;
    private BufferedImage right;
    private BufferedImage left;
    private BufferedImage idled;
    private boolean idle;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private Animation animation;
    private Animation idleAnimation;
    private long rollCd;
    private long last;
    private boolean[] pressedKeys;

    public Player() {
        facingRight = true;
        idle = true;
        xCoord = 50;  // Starting position
        yCoord = 435; // On the ground
        rollCd = 2000;
        last = 0;

        // Initialize the key tracking array
        pressedKeys = new boolean[256];

        // Load Idle Animation
        ArrayList<BufferedImage> idleImages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\idled" + i + ".png";
            try {
                idleImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading idle image: " + filename);
            }
        }
        idleAnimation = new Animation(idleImages, 200);

        // Load Walking Animation
        ArrayList<BufferedImage> walkImages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\Walkd" + i + ".png";
            try {
                walkImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        animation = new Animation(walkImages, 100);
    }

    public void cooldown() {
        long time = System.currentTimeMillis();
        if (time > last + rollCd) {
            last = time;
        }
    }

    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public void moveRight() {
        if (xCoord + MOVE_AMT <= 920) {
            xCoord += MOVE_AMT;
        }
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
    }

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            yCoord -= MOVE_AMT;
        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += MOVE_AMT;
        }
    }

    public void roll(String direction) {
        double a = xCoord;
        double b = yCoord;
        if (direction.equals("left")) {
            while (xCoord >= 0 && xCoord > a - 10) {
                xCoord -= 0.0000001;
                cooldown();
                last = 0;
            }
        } else if (direction.equals("right")) {
            while (xCoord <= 920 && xCoord < a + 10) {
                xCoord += 0.000001;
                cooldown();
                last = 0;
            }
        } else if (direction.equals("up")) {
            while (yCoord > 0 && yCoord > b - 10) {
                yCoord -= 0.000001;
                cooldown();
                last = 0;
            }
        } else if (direction.equals("down")) {
            while (yCoord < 435 && yCoord < b + 10) {
                yCoord += 0.000001;
                cooldown();
                last = 0;
            }
        }
    }

    public BufferedImage getPlayerImage() {
        // If moving, use the walking animation; if idle, use the idle animation
        if (isMoving()) {
            return animation.getActiveFrame();
        } else {
            return idleAnimation.getActiveFrame();
        }
    }

    // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Mark the key as pressed
        pressedKeys[e.getKeyCode()] = true;

        // If any movement key is pressed, switch to walking animation
        if (isMoving()) {
            idle = false;  // Player is no longer idle when moving
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Mark the key as released
        pressedKeys[e.getKeyCode()] = false;

        // If no movement keys are pressed, switch to idle animation
        if (!isMoving()) {
            idle = true;  // Player is idle when no movement key is pressed
        }
    }

    // Check if the player is moving (by checking movement keys)
    private boolean isMoving() {
        return pressedKeys[KeyEvent.VK_LEFT] ||
                pressedKeys[KeyEvent.VK_RIGHT] ||
                pressedKeys[KeyEvent.VK_UP] ||
                pressedKeys[KeyEvent.VK_DOWN];
    }
}
