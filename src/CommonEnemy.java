import java.awt.image.BufferedImage;

public class CommonEnemy extends Enemy {
    // Constructor for CommonEnemy
    public CommonEnemy(int hp, int dmg, double speed, double dmgD , double spawnx,double spawny, String aliveImagePath) {
        super(hp, dmg, speed, dmgD ,spawnx,spawny, aliveImagePath);
    }

    // Override moveTowardsPlayer for basic movement
    @Override
    public void moveTowardsPlayer(Player player) {
        double playerX = player.getxCoord();
        double playerY = player.getyCoord();

        double dx = playerX - xCoord;
        double dy = playerY - yCoord;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize direction
        dx /= distance;
        dy /= distance;

        // Move enemy towards player (speed determines how fast it moves)
        xCoord += dx * speed;
        yCoord += dy * speed;
    }

}
