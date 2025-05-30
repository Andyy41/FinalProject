public class Enemy {
    private int hp;
    private int dmg;
    private int speed;
    private double dmgD;

    public Enemy (int hp, int dmg, int speed, double dmgD) {
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.dmgD = dmgD;
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
}
