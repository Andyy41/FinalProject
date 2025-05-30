import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Player {
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
    private Animation frontRoll;
    private Animation DDR;
    private Animation DDL;
    private Animation UP;
    private Animation RIGHT;
    private Animation LEFT;
    private double rollCd;
    int a;
    private boolean b;

    public Player() {
        b = true;
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

        // Diagonal UP LEFT
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

        ArrayList<BufferedImage> frontRollImages = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String filename = "src\\frontRoll" + i + ".png";
            try {
                frontRollImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading idle image: " + filename);
            }
        }
        frontRoll = new Animation(frontRollImages, 100);

    ArrayList<BufferedImage> diagonalDL = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
        String filename = "src\\diagonalDL" + i + ".png";
        try {
            diagonalUR.add(ImageIO.read(new File(filename)));
        } catch (IOException e) {
            System.out.println("Error loading walk image: " + filename);
        }
    }
    DDL = new Animation(diagonalDL, 100);

    //Diagonal Down Left
    ArrayList<BufferedImage> diagonalDR = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
        String filename = "src\\diagonalDR" + i + ".png";
        try {
            diagonalUR.add(ImageIO.read(new File(filename)));
        } catch (IOException e) {
            System.out.println("Error loading walk image: " + filename);
        }
    }
    DDR = new Animation(diagonalUL, 100);
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

    public BufferedImage getPlayerImage(boolean isMoving, boolean isDiagonalU, boolean isDiagonalD, boolean roll, boolean facingRight , boolean facingUp) {
        if (b) {
            frontRoll.resetAnim();
            rollAnimation.resetAnim();
            b = false;
        }
        // If moving, use the walking animation; if idle, use the idle animation
        String Pos = "";
        if (roll) {
            if (facingUp) {
                return frontRoll.getActiveFrame();
            }
            return rollAnimation.getActiveFrame();
        }
        if (isMoving) {
            if (isDiagonalU && facingRight) {
                Pos = "DUR";
            } else Pos = "DUL";
            if (isDiagonalD && facingRight) {
                Pos = "DDR"; // DDR
            } else
                Pos = "DDL"; // DDL
            if (facingUp) {
                Pos = "Up";
            } else Pos = "DOWN";// WD
            if (facingRight) {
                Pos = "RIGHT";//WR
            } else Pos = "Left";// WL
            return Pos +
        }
        return idleAnimation.getActiveFrame();
    }


        // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect () {
        int imageHeight = getPlayerImage(true, false, false, false , false , false).getHeight();
        int imageWidth = getPlayerImage(true, false, false, false , false , false).getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }

    public void resetB () {
        b = true;
    }
