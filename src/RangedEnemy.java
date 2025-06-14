import java.awt.image.BufferedImage;
import java.util.Random;

public class RangedEnemy extends Enemy {
    private long lastShotTime = 0;
    private long shootCooldown = 250 + new Random().nextInt(251); // 250–500 ms
    private boolean hasShotThisTick = false;

    // Constructor for RangedEnemy
    public RangedEnemy(int hp, int dmg, double speed, double dmgD,double spawnx , double spawny , String aliveImagePath) {
        super(hp, dmg, speed, dmgD,spawnx ,spawny , aliveImagePath);
    }

    // Override moveTowardsPlayer for ranged behavior (for example, move slowly or patrol)
    @Override
    public void moveTowardsPlayer(Player player) {
        double playerX = player.getxCoord();
        double playerY = player.getyCoord();

        double dx = playerX - xCoord;
        double dy = playerY - yCoord;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // If the enemy is too far, it doesn't move directly
        if (distance > 200) {
            dx /= distance;
            dy /= distance;
            xCoord += dx * speed;
            yCoord += dy * speed;
        }
    }

        public EnemyProjectile shootAt(Player player, String projectileImage) {
            return new EnemyProjectile(xCoord + 16, yCoord + 16, player.getxCoord(), player.getyCoord(), projectileImage);
        }

    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();

        // Check if the cooldown has passed and the enemy hasn't shot this tick yet
        if (currentTime - lastShotTime >= shootCooldown) {
            lastShotTime = currentTime; // Update last shot time
            shootCooldown = 2000 ; // Random cooldown between 2000-2500 ms
            hasShotThisTick = false; // Reset shot flag
            return true;
        }

        return false;
    }



    public void resetShotFlag() {
        hasShotThisTick = false;
    }
    }
