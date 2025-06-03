import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private Animation upRoll;
    private double rollCd;
    private Animation diagRoll;
    int a;
    private boolean b;

    public Player() {
        b = true;
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
                diagonalUL.add(ImageIO.read(new File(filename)));
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
    //down left
        ArrayList<BufferedImage> diagonalDL = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\diagonalDL" + i + ".png";
            try {
                diagonalDL.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        DDL = new Animation(diagonalDL, 100);

        //Diagonal Down Right
        ArrayList<BufferedImage> diagonalDR = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\diagonalDR" + i + ".png";
            try {
                diagonalDR.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        DDR = new Animation(diagonalDR, 100);

        //UP
        ArrayList<BufferedImage> UPR = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\UP" + i + ".png";
            try {
                UPR.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        UP = new Animation(UPR, 100);

        //RIGHT
        ArrayList<BufferedImage> Right = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\Right" + i + ".png";
            try {
                Right.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
        RIGHT = new Animation(Right, 100);

        //LEFT
        ArrayList<BufferedImage> Left = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String filename = "src\\Left" + i + ".png";
            try {
                Left.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading walk image: " + filename);
            }
        }
       LEFT = new Animation(Left, 100);
        ArrayList<BufferedImage> upRollImages = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String filename = "src\\UpRoll" + i + ".png";
            try {
                upRollImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading idle image: " + filename);
            }
        }
        upRoll = new Animation(upRollImages, 100);
        ArrayList<BufferedImage> diagRollImages = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String filename = "src\\DiagRoll" + i + ".png";
            try {
                diagRollImages.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading idle image: " + filename);
            }
        }
        diagRoll = new Animation(diagRollImages, 100);
    }





    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
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

    public BufferedImage getPlayerImage(boolean isMoving, boolean isDiagonalU, boolean isDiagonalD, boolean roll, boolean facingRight,boolean facingLeft , boolean facingUp , boolean facingDown) {
        if (b) {
            frontRoll.resetAnim();
            rollAnimation.resetAnim();
            diagRoll.resetAnim();
            upRoll.resetAnim();
            b = false;
        }
        // If moving, use the walking animation; if idle, use the idle animation
        if (roll) {
            if (facingUp) {
                return upRoll.getActiveFrame();
            } else if (isDiagonalU && facingRight) {
                return diagRoll.getActiveFrame();
            } else if (facingDown) {
                return frontRoll.getActiveFrame();
            } else if (facingRight) {
                return rollAnimation.getActiveFrame();
            } else if (facingLeft) {
                return rollAnimation.getActiveFrame();
            }
        }
        if (isMoving) {
            if (isDiagonalU && facingRight) {
                return DUR.getActiveFrame();
            } else if (isDiagonalU) {
                return DUL.getActiveFrame();
            } else if (isDiagonalD && facingRight) {
                return DDR.getActiveFrame(); // DDR
            } else if (isDiagonalD) {
                return DDL.getActiveFrame(); // DDL
            } else if (facingUp) {
                return UP.getActiveFrame();
            } else if (facingDown){
                    return animation.getActiveFrame();
            } else if (facingRight) {
                return RIGHT.getActiveFrame(); // WR
            } else if(facingLeft)
                return LEFT.getActiveFrame(); // WL
            }
        return idleAnimation.getActiveFrame();
    }


        // We use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage(true, false, false, false, false, false, false, false).getHeight();
        int imageWidth = getPlayerImage(true, false, false, false, false, false,false,false).getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }

    public void resetB() {
        b = true;
    }
}