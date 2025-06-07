import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProjectileBase {
    protected double x, y;
    protected double dx, dy;
    protected double speed;
    protected BufferedImage bullet;

    public ProjectileBase(double x, double y, double targetX, double targetY, double speed, String image) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        double angle = Math.atan2(targetY - y, targetX - x);
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;

        try {
            bullet = ImageIO.read(new File(image));
        } catch (IOException e) {
            System.out.println("Failed to load alive sprite: " + e.getMessage());
        }
    }

public void update() {
    x += dx;
    y += dy;
}

public void draw(Graphics g) {
    g.drawImage(bullet, (int) x, (int) y, null);
}

public Rectangle getBounds() {
    return new Rectangle((int) x, (int) y, bullet.getWidth(), bullet.getHeight());
}

public boolean isOffScreen(int width, int height) {
    return x < 0 || y < 0 || x > width || y > height;
}

public double getX() { return x; }
public double getY() { return y; }
}
