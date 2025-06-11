import java.awt.image.BufferedImage;

public class EnemyProjectile extends ProjectileBase {
    public EnemyProjectile(double x, double y, double targetX, double targetY,String image) {
        super(x, y, targetX, targetY, 2, image); // Slightly slower for enemy
    }
}
