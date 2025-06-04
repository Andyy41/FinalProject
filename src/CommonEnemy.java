import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CommonEnemy extends Enemy{
    private BufferedImage aliveSprite;
    private BufferedImage DEAD;
    public CommonEnemy(int hp, int dmg, int speed, double dmgD) {
        super(hp, dmg, speed, dmgD);
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
        return super.getxCoord();
    }

    public double getyCoord() {
        return super.getyCoord();
    }

    public int getHp () {
        return super.getHp();
    }

    public int getDMG(){
        return super.getDmg();
    }
    public Rectangle EnemyRect() {
      int imageHeight = super.getEnemyImage(true).getHeight();
        int imageWidth = super.getEnemyImage(true).getWidth();
        Rectangle rect = new Rectangle((int) super.getxCoord(), (int) super.getyCoord(), imageWidth, imageHeight);
        return rect;
    }

}
