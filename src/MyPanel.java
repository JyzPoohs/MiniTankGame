import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

@SuppressWarnings("all")
public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;
    Vector<EnemyTank> enemyTanks = new Vector<>(); // 存放敌人坦克
    Vector<Node> nodes = new Vector<>(); // 用于恢复敌人坦克信息
    Vector<Bomb> bombs = new Vector<>(); // 存放炸弹
    int enemySize = 3;
    // 定义三张 炸弹图片
    Image i1 = null;
    Image i2 = null;
    Image i3 = null;

    public MyPanel(String key) { // 通过构造器 初始化玩家坦克
        File file = new File(Recorder.getRecordFile());
        if (key.equals("2") && file.exists()) // 判断 保存数据的文件 是否存在
            nodes = Recorder.getNodes(); // 如果存在，获取之前的游戏记录
        else { // 如果文件不存在 就 开启新游戏
            System.out.println("File doesn't exist, open new game");
            key = "1";
        }

        Recorder.setEnemyTanks(enemyTanks); // 把MyPanel的 enemyTanks 设置给 Recorder的 enemyTanks

        hero = new Hero(500, 100); // 创建玩家坦克， 传入初始位置
        hero.setSpeed(3); // 设置坦克速度

        switch (key) {
            case "1":
                for (int i = 0; i < enemySize; i++) {
                    // 先创建敌人坦克 再设置初始 面向方向，每个坦克之间距离100
                    EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                    enemyTank.setEnemyTanks(enemyTanks); // 将MyPanel的 enemyTanks 设置给 EnemyTank类的 enemyTanks
                    enemyTank.setDirect(2); // 设置敌人坦克 面对 玩家
                    new Thread(enemyTank).start(); // 启动敌人坦克

                    // 绘制 敌人坦克的 子弹
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot); // 加入 子弹 到敌人的Vector里
                    new Thread(shot).start(); // 启动 敌人子弹的 线程

                    enemyTanks.add(enemyTank); // 加入坦克到Vector里break;
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    // 先创建敌人坦克 再设置初始 面向方向，每个坦克之间距离100
                    EnemyTank enemyTank = new EnemyTank(node.getX(), 0);
                    enemyTank.setEnemyTanks(enemyTanks); // 将MyPanel的 enemyTanks 设置给 EnemyTank类的 enemyTanks
                    enemyTank.setDirect(node.getDirect()); // 设置敌人坦克 面对 玩家
                    new Thread(enemyTank).start(); // 启动敌人坦克

                    // 绘制 敌人坦克的 子弹
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot); // 加入 子弹 到敌人的Vector里
                    new Thread(shot).start(); // 启动 敌人子弹的 线程

                    enemyTanks.add(enemyTank); // 加入坦克到Vector里break;
                }
                break;
            default:
                System.out.println("Wrong input");
        }

        // 初始化图片对象
        i1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img/bomb2.gif"));
        i2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img/bomb.gif"));
        i3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img/bomb2.gif"));
    }

    // 显示 玩家 击败敌人坦克的 信息
    public void showInfo(Graphics g) {
        g.setColor(Color.BLACK);
        Font f = new Font("宋体", Font.BOLD, 25);
        g.setFont(f);

        g.drawString("累积击毁敌人坦克", 1020, 30);
        drawTank(1020, 60, g, 0, 0); // 画出敌人坦克
        g.setColor(Color.BLACK); // 必须重设，因为drawTank时 改变 画笔 g 为 cyan
        g.drawString(Recorder.getAllEnemyTankNum() + "", 1080, 100);
    }

    @Override // MyPanel对象是一个画板，g是 一支画笔，Graphics 提供绘图方法
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750); // 填充矩形，默认 背景黑色
        showInfo(g); // 显示 击毁敌人坦克信息

        // 绘制 玩家坦克
        if (hero != null && hero.isLive)
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
        // 绘制 玩家坦克 子弹
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot s = hero.shots.get(i);
            if (s != null && s.isLive) {
                g.fill3DRect(hero.shot.x, hero.shot.y, 3, 3, false);
            } else
                hero.shots.remove(s);
        }

        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i); // 取出炸弹
            if (bomb.life > 6) {
                g.drawImage(i1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(i2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(i3, bomb.x, bomb.y, 60, 60, this);
            }

            bomb.lifeDown(); // 减少炸弹的 生命值
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

        // 绘制 敌人坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i); // 取出敌人坦克

            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);

                // 画出所有 敌人子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) { // 绘制 敌人子弹
                        g.draw3DRect(shot.x, shot.y, 3, 3, false);
                    } else {
                        enemyTank.shots.remove(shot); // 移除子弹
                    }
                }
            }
        }
    }

    /**
     * d
     *
     * @param x      坦克 左上角 横坐标
     * @param y      坦克 左上角 直坐标
     * @param g      画笔
     * @param direct 方向
     * @param type   类型（玩家/敌人坦克）
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0: // 敌人坦克 设置为 蓝青色
                g.setColor(Color.cyan);
                break;
            case 1: // 玩家坦克 设置为 黄色
                g.setColor(Color.yellow);
                break;
        }

        // 根据坦克方向 绘制坦克：0 向上，1 向右， 2
        switch (direct) {
            case 0: // 向上
                g.fill3DRect(x, y, 10, 60, false); // 左边轮子
                g.fill3DRect(x + 30, y, 10, 60, false); // 右边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // 中间躯体
                g.fillOval(x + 10, y + 20, 20, 20); // 圆形炮台
                g.drawLine(x + 20, y + 30, x + 20, y); // 射击管
                break;
            case 1: // 向右
                g.fill3DRect(x, y, 60, 10, false); // 左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false); // 右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // 中间躯体
                g.fillOval(x + 20, y + 10, 20, 20); // 圆形炮台
                g.drawLine(x + 30, y + 20, x + 60, y + 20); // 射击管
                break;
            case 2: // 向下
                g.fill3DRect(x, y, 10, 60, false); // 左边轮子
                g.fill3DRect(x + 30, y, 10, 60, false); // 右边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // 中间躯体
                g.fillOval(x + 10, y + 20, 20, 20); // 圆形炮台
                g.drawLine(x + 20, y + 30, x + 20, y + 60); // 射击管
                break;
            case 3: // 向左
                g.fill3DRect(x, y, 60, 10, false); // 左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false); // 右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // 中间躯体
                g.fillOval(x + 20, y + 10, 20, 20); // 圆形炮台
                g.drawLine(x + 30, y + 20, x, y + 20); // 射击管
                break;
            default:
                System.out.println(" ");
        }

    }

    // 判断子弹 是否 击中 坦克（不分玩家还是敌人的）
    public void hitTank(Shot s, Tank e) { // Tank类型为 玩家和敌人坦克的父类
        switch (e.getDirect()) {
            case 0: // Up
            case 2: // Down
                if (s.x > e.getX() && s.x < e.getX() + 40
                        && s.y > e.getY() && s.y < e.getY() + 60) {
                    s.isLive = false;
                    e.isLive = false;

                    // 玩家击毁 敌人坦克，就移除坦克，增加 击毁数量
                    enemyTanks.remove(e);
                    if (e instanceof EnemyTank)
                        Recorder.addAllEnemyTankNum();

                    // 创建Bomb对象，加入bombs集合
                    Bomb b = new Bomb(e.getX(), e.getY());
                    bombs.add(b);
                }
                break;
            case 1: // Right
            case 3: // Left
                if (s.x > e.getX() && s.x < e.getX() + 60
                        && s.y > e.getY() && s.y < e.getY() + 40) {
                    s.isLive = false;
                    e.isLive = false;

                    // 玩家击毁 敌人坦克，就移除坦克，增加 击毁数量
                    enemyTanks.remove(e);
                    if (e instanceof EnemyTank)
                        Recorder.addAllEnemyTankNum();

                    Bomb b = new Bomb(e.getX(), e.getY());
                    bombs.add(b);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) { // 上
            hero.setDirect(0);
            if (hero.getY() > 0)
                hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) { // 右
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000)
                hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) { // 下
            hero.setDirect(2);
            if (hero.getY() + 60 < 750)
                hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) { // 左
            hero.setDirect(3);
            if (hero.getX() > 0)
                hero.moveLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_J) { // J键发射
            if (hero.shot == null || !hero.shot.isLive)
                hero.shotEnemy();
        }

        this.repaint(); // 重写调用Paint，并修改方向

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() { // 每隔100毫秒 重绘区域，刷新子弹
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            hitEnemyTank(); // 判断玩家坦克是否 击中敌人坦克
            hitHero(); // 判断敌人坦克是否 击中玩家坦克
            this.repaint();
        }
    }

    public void hitEnemyTank() { // 遍历玩家子弹集合 是否 击中 敌人子弹
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot s = hero.shots.get(j);
            if (s != null && s.isLive) { // 如果玩家子弹还存活，就遍历 敌人坦克
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank e = enemyTanks.get(i);
                    hitTank(s, e);
                }
            }
        }
    }

    public void hitHero() { // 判断子弹是否击中 玩家坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank e = enemyTanks.get(i);
            for (int j = 0; j < e.shots.size(); j++) {
                Shot s = e.shots.get(j);
                if (hero.isLive && s.isLive)
                    hitTank(s, hero);
            }
        }
    }
}
