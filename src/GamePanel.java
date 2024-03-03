import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;
import java.security.Key;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNIT=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=75;
    final int[] x=new int[GAME_UNIT];
    final int[] y=new int[GAME_UNIT];
    int bodySnake=6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction='R';
    boolean running=false;
    Random random;
    Timer timer;
    GamePanel(){
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
           super.paintComponent(g);
           draw(g);
    }
    public void newApple(){
        appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void draw(Graphics g) {
        if (running) {
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodySnake; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("ink free",Font.BOLD,40));
            FontMetrics fontMetrics=getFontMetrics(g.getFont());
            g.drawString("Score:"+appleEaten,(SCREEN_WIDTH-fontMetrics.stringWidth("Score:"+appleEaten))/2,g.getFont().getSize());

        }else {
            gameOver(g);
        }
    }
    public void move(){
        for (int i=bodySnake;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch (direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
        }

    }
    public void checkApple(){
        if (x[0]==appleX&&y[0]==appleY){
            bodySnake++;
            appleEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //check if head collides with body
        for (int i=bodySnake;i>0;i--){
            if(x[0]==x[i]&&y[0]==y[i]){
                running=false;
            }
        }
        //check if the head touches left border
        if(x[0]<0){
            running=false;
        }
        //check if the head touches right border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //check if the head touches top border
        if(y[0]<0){
            running=false;
        }
        //check if the head touches bottom border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        if (!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("ink free",Font.BOLD,75));
        FontMetrics fontMetrics=getFontMetrics(g.getFont());
        g.setColor(Color.red);
        g.setFont(new Font("ink free",Font.BOLD,40));
        FontMetrics fontMetrics1=getFontMetrics(g.getFont());
        g.drawString("Score:"+appleEaten,(SCREEN_WIDTH-fontMetrics1.stringWidth("Score:"+appleEaten))/2,g.getFont().getSize());
        g.drawString("Game Over",(SCREEN_WIDTH-fontMetrics1.stringWidth("Game Over"))/2,(SCREEN_HEIGHT/2));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}
