import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

public class Enemy {
    private int hp;
    private int dmg;
    private int speed;
    private double dmgD;
    private boolean alive;
    private double xCoord;
    private double yCoord;
    private BufferedImage aliveSprite;
    private BufferedImage DEAD;

    public Enemy (int hp, int dmg, int speed, double dmgD) {
       xCoord = 200;
       yCoord = 200;
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.dmgD = dmgD;
        try {
            aliveSprite = ImageIO.read(new File("src\\Enemy.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            DEAD = ImageIO.read(new File("src\\EnemyDeath.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void Spawn(){
        try {
            aliveSprite = ImageIO.read(new File("src\\Enemy.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public int getHp () {
        return hp;
    }

    public int getDmg() {
        return dmg;
    }

    public int getSpeed() {
        return speed;
    }

    public double getDmgD() {
        return dmgD;
    }

    public void decreaseHp(int dec) {
        hp -= dec;
    }

    public boolean dead() {
        return hp <= 0;
    }

    public BufferedImage getEnemyImage(boolean alive){
        if (alive){
        return aliveSprite;
        }
        return DEAD;
    }
    public Rectangle EnemyRect() {
        int imageHeight = getEnemyImage(alive).getHeight();
        int imageWidth = getEnemyImage(alive).getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
