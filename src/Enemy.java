import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public  class Enemy {
    protected int hp;
    protected int dmg;
    protected int speed;
    protected double dmgD;
    protected boolean alive;
    protected double xCoord;
    protected double yCoord;
    protected BufferedImage aliveSprite;
    protected BufferedImage deadSprite;

    public Enemy(int hp, int dmg, int speed, double dmgD, String aliveImagePath, String deadImagePath) {
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.dmgD = dmgD;
        this.xCoord = 200;
        this.yCoord = 200;
        this.alive = true;

        try {
            aliveSprite = ImageIO.read(new File(aliveImagePath));
        } catch (IOException e) {
            System.out.println("Failed to load alive sprite: " + e.getMessage());
        }

        try {
            deadSprite = ImageIO.read(new File(deadImagePath));
        } catch (IOException e) {
            System.out.println("Failed to load dead sprite: " + e.getMessage());
        }
    }

    public void decreaseHp(int dec) {
        hp -= dec;
        if (hp <= 0) alive = false;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public BufferedImage getCurrentImage() {
        return alive ? aliveSprite : deadSprite;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle((int) xCoord, (int) yCoord, aliveSprite.getWidth(), aliveSprite.getHeight());
    }

    public double getX() { return xCoord; }
    public double getY() { return yCoord; }
    public int getHp() { return hp; }
    public int getDmg() { return dmg; }
    public int getSpeed() { return speed; }
    public double getDmgD() { return dmgD; }

    public void setX(double x) { xCoord = x; }
    public void setY(double y) { yCoord = y; }
}
