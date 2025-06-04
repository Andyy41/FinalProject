import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private BufferedImage background;
    private BufferedImage block;
    private Timer timer;
    private Player player;
    private Enemy enemy;
    private boolean[] pressedKeys;
    private double cd;
    private double baseCd = 270;
    boolean rolled;
    boolean right;
    boolean left;
    boolean up;
    boolean down;
    int a = 0;
    String direction;


    public GraphicsPanel() {
        timer = new Timer(5, this);
        timer.start();
        cd = 0;
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            block = ImageIO.read(new File("src/BLOCK.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player();
        enemy = new Enemy(10,1,2,1);
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    // Check if the player is moving (by checking movement keys)
    private boolean isMoving() {
        boolean x = pressedKeys[KeyEvent.VK_A] ||
                pressedKeys[KeyEvent.VK_D] ||
                pressedKeys[KeyEvent.VK_W] ||
                pressedKeys[KeyEvent.VK_S];
        //System.out.println(x);
        return x;
    }

    private boolean isDiagonalU(){
        boolean c = pressedKeys[KeyEvent.VK_W] && (pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_D]);
        //System.out.println("IS DU");
        return c;
    }
    private boolean isDiagonalD(){
        boolean r = pressedKeys[KeyEvent.VK_S] && (pressedKeys[KeyEvent.VK_A] || pressedKeys[KeyEvent.VK_D]);
        //System.out.println("IS DD");
        return r;
    }

    private boolean isUP(){
        boolean w = pressedKeys[KeyEvent.VK_W];
        //System.out.println("IS UP");
        return w;
    }

    private boolean isDown(){
        boolean s = pressedKeys[KeyEvent.VK_S];
        //System.out.println("IS DOWN");
        return s;
    }

    private boolean isRight(){
        boolean d = pressedKeys[KeyEvent.VK_D];
        //System.out.println("IS RIGHT");
        return d;
    }

    private boolean isLeft(){
        boolean a = pressedKeys[KeyEvent.VK_A];
        //System.out.println("IS LEFT");
        return a;
    }

    private boolean isEnemyAlive(){
        return enemy.getHp() > 0;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // the order that things get "painted" matter; we paint the background first
        g.drawImage(background, 0, 0, null);
        if (a == 0) {
            player.resetB();
        }
        if (rolled) {
            if (direction.equals("down")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, false, false, true, right, left,up,true), (int) player.getxCoord(), (int) player.getyCoord(), null);
                    a++;
                } else {
                    rolled = false;
                }
            } else if (direction.equals("upr")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, true, false, true, true,left,up,down), (int) player.getxCoord(), (int) player.getyCoord(), null);
                    a++;
                } else {
                    rolled = false;
                }
            } else if (direction.equals("up")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, false, false, true, right,left,true,down), (int) player.getxCoord(), (int) player.getyCoord(), null);
                    a++;
                } else {
                    rolled = false;
                }
            } else if (direction.equals("right")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, false, false, true, true,left,up,down), (int) player.getxCoord(), (int) player.getyCoord(), null);
                    a++;
                } else {
                    rolled = false;}
            } else if (direction.equals("left")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, false, false, true, false,true,false,false), (int) player.getxCoord(), (int) player.getyCoord(), -16, 15, null);
                    a++;
                } else {
                    rolled = false;}
            } else if (direction.equals("upl")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, true, false, true, true,left,up,down), (int) player.getxCoord() + 13, (int) player.getyCoord() + 7, -16, 15, null);
                    a++;
                } else {
                    rolled = false;
                }
            } else if (direction.equals("dr")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, true, false, true, true,left,up,down), (int) player.getxCoord(), (int) player.getyCoord(), 16, -15,  null);
                    a++;
                } else {
                    rolled = false;
                }
            } else if (direction.equals("dl")) {
                if (a < 85) {
                    g.drawImage(player.getPlayerImage(false, false, true, true, false,true,up,down), (int) player.getxCoord(), (int) player.getyCoord(), -16, 15, null);
                    a++;
                } else {
                    rolled = false;
                }
            }
        }
        if (!rolled) {
            g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), false , isRight() , isLeft(), isUP(),isDown()), (int) player.getxCoord(), (int) player.getyCoord(), null);
            g.drawImage(player.getPlayerImage(isMoving(), isDiagonalU(), isDiagonalD(), false ,isRight() , isLeft(), isUP(),isDown()), (int) player.getxCoord(), (int) player.getyCoord(), null);
        }
        if(isEnemyAlive()){
            g.drawImage(enemy.getEnemyImage(isEnemyAlive()), (int) enemy.getxCoord(), (int) enemy.getyCoord(), null);
        }
        if(player.playerRect().intersects(CommonEnemy.EnemyRect())){
            player.Hit();
        }
        g.drawImage(block, 50, 10, null);
        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.setColor(Color.red);
        g.drawString(Double.toString(cd), 50, 50);
        g.setFont(new Font("Times New Roman", Font.BOLD, 22));
        g.setColor(Color.white);
        g.drawString("Cooldowns", 50, 30);


        // this loop does two things:  it draws each Coin that gets placed with mouse clicks,
        // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
        // the score goes up and the Coin is removed from the arraylist

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));


        // player moves left (A)
        if (pressedKeys[65]) {
            player.moveLeft();
        }


        // player moves right (D)
        if (pressedKeys[68]) {
            player.moveRight();
        }


        // player moves up (W)
        if (pressedKeys[87]) {
            player.moveUp();
        }


        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }

        if (cd == 0) {
            if (pressedKeys[65] && pressedKeys[67]) {
                direction = "left";
                a = 0;
                rolled = true;
                player.roll(direction);
                cd = baseCd;
            }

            if (pressedKeys[68] && pressedKeys[67]) {
                direction = "right";
                a = 0;
                rolled = true;
                player.roll(direction);
                cd = baseCd;
            }

            if (pressedKeys[87] && pressedKeys[67]) {
                if (pressedKeys[68]) {
                    direction = "upr";
                    a = 0;
                    rolled = true;
                    player.roll("up");
                    player.roll("right");
                    cd = baseCd;
                } else if (pressedKeys[65]) {
                    direction = "upl";
                    a = 0;
                    rolled = true;
                    player.roll("up");
                    player.roll("left");
                    cd = baseCd;
                } else {
                    direction = "up";
                    a = 0;
                    rolled = true;
                    player.roll(direction);
                    cd = baseCd;
                }
            }

            if (pressedKeys[67]) {
                if (pressedKeys[83]) {
                    System.out.println("ahgajhg");
                    if (pressedKeys[68]) {
                        System.out.println("test");
                        direction = "dr";
                        a = 0;
                        rolled = true;
                        player.roll("down");
                        player.roll("right");
                        cd = baseCd;
                    } else if (pressedKeys[65]) {
                        System.out.println("a");
                        direction = "dl";
                        a = 0;
                        rolled = true;
                        player.roll("down");
                        player.roll("left");
                        cd = baseCd;
                    } else {
                        System.out.println("r");
                        direction = "down";
                        a = 0;
                        rolled = true;
                        player.roll(direction);
                        cd = baseCd;
                    }
                }
                System.out.println("ghagghagskhgkahgk");
            }
        }
        if (cd > 0) {
            cd--;
        }
    }

  //  public ArrayList<Integer> Keys(){
  //      while (isMoving())
  //  }

    // ActionListener interface method
    @Override
    public void actionPerformed(ActionEvent e) {
        // repaints the window every 10ms
        repaint();
    }


    // KeyListener interface methods
    @Override
    public void keyTyped(KeyEvent e) { } // unimplemented


    @Override
    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }


    // MouseListener interface methods
    @Override
    public void mouseClicked(MouseEvent e) { }  // unimplemented because
    // if you move your mouse while clicking, this method isn't
    // called, so mouseReleased is best


    @Override
    public void mousePressed(MouseEvent e) { } // unimplemented


    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented


    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented
}
