import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Enemy {
    int hp;
    int dmg;
    double speed;
    double dmgD;
    boolean alive;
    double xCoord;
    double yCoord;
    double spawnx;
    double spawny;
    BufferedImage aliveSprite;
    BufferedImage deadSprite;


    public Enemy(int hp, int dmg, double speed, double dmgD, double spawnx, double spawny, String aliveImagePath) {
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.dmgD = dmgD;
        this.alive = true;
        this.spawnx = spawnx;
        this.spawny = spawny;

        try {
            aliveSprite = ImageIO.read(new File(aliveImagePath));
        } catch (IOException e) {
            System.out.println("Failed to load alive sprite: " + e.getMessage());
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
    public double getSpeed() { return speed; }
    public double getDmgD() { return dmgD; }
    public double getSpawnx(){return spawnx;}
    public double getSpawny(){return spawny;}


    public void setX(double x) { xCoord = x; }
    public void setY(double y) { yCoord = y; }

    // Override moveTowardsPlayer for basic movement
    public abstract void moveTowardsPlayer(Player player);
}
