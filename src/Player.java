import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player  {
    private final int MOVE_AMT = 2;
    private BufferedImage right;
    private BufferedImage left;
    private BufferedImage idled;
    private boolean idle;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private Animation animation;
    private Animation idleAnimation;
    private Animation rollAnimation;
    private Animation DUR;
    private Animation DUL;
    private double rollCd;
    int a;

    public Player() {
        facingRight = true;
        idle = true;
        xCoord = 50;  // Starting position
        yCoord = 435; // On the ground
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

        // Diagonal UP RIGHT
        ArrayList<BufferedImage> diagonalUR = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\diagonalUR" + i + ".png";
            try {
                diagonalUR.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        DUR = new Animation(diagonalUR, 100);

        //Roll
        ArrayList<BufferedImage> rollImages = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String filename = "src\\Roll" + i + ".png";
            try {
                rollImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading idle image: " + filename);
            }
        }
        rollAnimation = new Animation(rollImages, 100);

        ArrayList<BufferedImage> diagonalUL = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\diagonalUL" + i + ".png";
            try {
                diagonalUR.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        DUL = new Animation(diagonalUL, 100);





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
            if (xCoord >= 0 && xCoord > a - 10) {
                xCoord -= 10;
            }
        } else if (direction.equals("right")) {
            if (xCoord <= 920 && xCoord < a + 10) {
                xCoord += 10;
            }
        } else if (direction.equals("up")) {
            if (yCoord > 0 && yCoord > b - 10) {
                yCoord -= 10;
            }
        } else if (direction.equals("down")) {
            if (yCoord < 435 && yCoord < b + 10) {
                yCoord += 10;
            }
        }
    }

    public BufferedImage getPlayerImage(boolean isMoving , boolean isDiagonalU ,boolean isDiagonalD, boolean roll) {
        // If moving, use the walking animation; if idle, use the idle animation
        if (roll) {
            return rollAnimation.getActiveFrame();
        }
        if (isMoving) {
            if(isDiagonalU){
            return DUR.getActiveFrame();
            } else if (isDiagonalD){
            DUL.getActiveFrame();
            }
            return animation.getActiveFrame();
        } else {
            if (isDiagonalU) {
            return DUR.getActiveFrame();
            }
            else if (isDiagonalD) {
                return DUR.getActiveFrame();
            }
            return idleAnimation.getActiveFrame();
        }
    }

    // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage(true,false,false, false).getHeight();
        int imageWidth = getPlayerImage(true,false,false, false).getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
