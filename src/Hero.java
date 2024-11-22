import java.util.Vector;

public class Hero extends Tank {
    Shot shot = null; // 定义Shot对象，代表 射击（线程）
    Vector<Shot> shots = new Vector<>();

    public Hero(int x, int y) {
        super(x, y);
    }

    public void shotEnemy() {
        if (shots.size() == 5)
            return; // 一次 只能发射5颗子弹

        switch (getDirect()) {
            case 0: // Up
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1: // Right
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2: // Down
                shot = new Shot(getX() + 20, getY() + 20, 2);
                break;
            case 3: // Left
                shot = new Shot(getX(), getY() + 20, 3);
                break;
        }
        shots.add(shot); // 新创建的shot 放入集合中
        new Thread(shot).start(); // 启动Shot线程
    }

}
