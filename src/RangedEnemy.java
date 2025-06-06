import java.awt.image.BufferedImage;

public class RangedEnemy extends Enemy {

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

        // If it's close enough, it could stop and attack
        // This could be expanded with a shooting mechanism (range attack)
    }
}
