public class CommonEnemy extends Enemy{

    public CommonEnemy(int hp, int dmg, int speed, double dmgD, String aliveImagePath, String deadImagePath) {
        super(hp, dmg, speed, dmgD, aliveImagePath, deadImagePath);
    }

    public int getDMG(){
        return dmg;
    }


}
