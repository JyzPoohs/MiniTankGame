import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class TankGame extends JFrame {
    MyPanel mp = null;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        TankGame game = new TankGame();
    }

    public TankGame() {
        System.out.print("Enter [1=New Game, 2=Continue] : ");
        String key = scanner.next();
        mp = new MyPanel(key);

        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp); //添加游戏 绘图区域
        this.addKeyListener(mp); //让JFrame 监听mp的 键盘事件
        this.setSize(1300, 800); //设置区域大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //点击关闭就结束程序
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() { //监听 窗口关闭事件
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord(); //关闭窗口时，记录 玩家 击毁 敌人坦克信息
                System.exit(0);
            }
        });
    }
}
