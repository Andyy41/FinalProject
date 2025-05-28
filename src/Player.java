import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Player  {
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

    public Player() {
        facingRight = true;
        idle = true;
        xCoord = 50;  // Starting position
        yCoord = 435; // On the ground
        rollCd = 2000;
        last = 0;

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

    ArrayList<BufferedImage> IdleUR = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
        String filename = "src\\IdleUR" + i + ".png";
        try {
            IdleUR.add(ImageIO.read(new File(filename)));
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

    public void roll(String direction) throws InterruptedException {
        double a = xCoord;
        double b = yCoord;
        if (direction.equals("left")) {
            if (xCoord >= 0 && xCoord > a - 10) {
                try {
                    Thread.sleep(50);
                    xCoord -= 1;
                } catch (InterruptedException e) {
                    // Handle the exception (e.g., log it or re-throw it)
                    e.printStackTrace();
                }
            }
        } else if (direction.equals("right")) {
            if (xCoord <= 920 && xCoord < a + 10) {
                try {
                    xCoord += 1;
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // Handle the exception (e.g., log it or re-throw it)
                    e.printStackTrace();
                }
            }
        } else if (direction.equals("up")) {
            if (yCoord > 0 && yCoord > b - 10) {
                try {
                    Thread.sleep(50);
                    yCoord -= 1;
                } catch (InterruptedException e) {
                    // Handle the exception (e.g., log it or re-throw it)
                    e.printStackTrace();
                }
            }
        } else if (direction.equals("down")) {
            if (yCoord < 435 && yCoord < b + 10) {
                try {
                    Thread.sleep(50);
                    yCoord += 1;
                } catch (InterruptedException e) {
                    // Handle the exception (e.g., log it or re-throw it)
                    e.printStackTrace();
                }
            }
        }
    }

    public BufferedImage getPlayerImage(boolean isMoving , boolean isDiagonalU ,boolean isDiagonalD) {
        // If moving, use the walking animation; if idle, use the idle animation
        if (isMoving) {
            if(isDiagonalU){

            } else if (isDiagonalD){

            }
            return animation.getActiveFrame();
        } else {
            if (isDiagonalU) {

            }
            else if (isDiagonalD) {
            }
            return idleAnimation.getActiveFrame();
        }
    }

    // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage(true,false,false).getHeight();
        int imageWidth = getPlayerImage(true,false,false).getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
