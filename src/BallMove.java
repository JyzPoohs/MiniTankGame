import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BallMove extends JFrame{
    BallPanel mp = null;
    public static void main(String[] args) {
        BallMove ball = new BallMove(); //创建 球
    }

    public BallMove(){
        mp = new BallPanel();
        this.add(mp);
        this.addKeyListener(mp); //JFrame对象 可监听 对BallPanel的 键盘事件
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
//KeyListener 监听 键盘事件
class BallPanel extends JPanel implements KeyListener {
    int x = 10, y = 10;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillOval(x, y, 20, 20);
    }

    @Override //有字符输出 就触发
    public void keyTyped(KeyEvent e) {

    }

    @Override //当某个键被 按下
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN){ //如果按下的键是 向下
            y++;
        }else if(e.getKeyCode() == KeyEvent.VK_UP){ //向上
            y--;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){ //往左
            x--;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){ //往右
            x++;
        }

        this.repaint(); //让画板 重绘
    }

    @Override //当某个键被 释放
    public void keyReleased(KeyEvent e) {

    }
}
