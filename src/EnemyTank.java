import java.util.Vector;

public class EnemyTank extends Tank implements Runnable {
    Vector<Shot> shots = new Vector<>();
    Vector<EnemyTank> enemyTanks = new Vector<>(); //MyPanel也有
    boolean isLive = true;

    public EnemyTank(int x, int y) {
        super(x, y);
    }

    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks; //设置MyPanel成员 给EnemyTank成员
    }

    @Override
    public void run() {
        while (true) {
            //坦克存活 且 没有子弹
            if (shots.size() < 1 && isLive) {
                Shot s = null;
                switch (getDirect()) {
                    case 0:
                        s = new Shot(getX() + 20, getY(), 0);
                        break;
                    case 1:
                        s = new Shot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2:
                        s = new Shot(getX() + 20, getY() + 60, 2);
                        break;
                    case 3:
                        s = new Shot(getX(), getY() + 20, 3);
                        break;
                }

                shots.add(s);
                new Thread(s).start();
            }

            switch (getDirect()) {
                case 0: //Up
                    for (int i = 0; i < 30; i++) { //让坦克保存一个方向 走30步
                        if (getY() > 0 && !isTouchEnemyTank()) moveUp();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1: //Right
                    for (int i = 0; i < 30; i++) {
                        if (getX() + 60 < 1000 && !isTouchEnemyTank()) moveRight();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2: //Down
                    for (int i = 0; i < 30; i++) {
                        if (getY() > 0 && !isTouchEnemyTank()) moveDown();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3: //Left
                    for (int i = 0; i < 30; i++) {
                        if (getX() > 0 && !isTouchEnemyTank()) moveLeft();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            setDirect((int) (Math.random() * 4)); //随机改变方向 0~3

            if (!isLive) break; //如果坦克不存活就 退出线程
        }
    }

    public boolean isTouchEnemyTank() { //判断 敌人坦克之间 有没有重叠
        switch (this.getDirect()) { //判断当前 敌人坦克方向
            case 0: //坦克面向 上
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i); //从Vector取出敌人坦克
                    if (enemyTank != this) { //不和自己比较, 对方坦克 面向 0=Up, 2=Down, 1=Left, 3=Right
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) { //上 下
                            //当前坦克 左上角坐标 [this.getX(), this.getY()]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右上角坐标 [this.getX() + 40, this.getY()]
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) { //左 右
                            //当前坦克 左上角坐标 [this.getX(), this.getY()]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右上角坐标 [this.getX() + 40, this.getY()]
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                    }
                }
                break;
            case 1: //坦克面向 右
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i); //从Vector取出敌人坦克
                    if (enemyTank != this) { //不和自己比较, 对方坦克 面向 0=Up, 2=Down, 1=Left, 3=Right
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) { //上 下
                            //当前坦克 右上角坐标 [this.getX() + 60, this.getY()]
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右下角坐标 [this.getX() + 60, this.getY() + 40]
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 40
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) { //左 右
                            //当前坦克 右上角坐标 [this.getX() + 60, this.getY()]
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右下角坐标 [this.getX() + 60, this.getY() + 40]
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 60
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                    }
                }
                break;
            case 2: //坦克面向 下
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i); //从Vector取出敌人坦克
                    if (enemyTank != this) { //不和自己比较, 对方坦克 面向 0=Up, 2=Down, 3=Left, 1=Right
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) { //上 下
                            //当前坦克 左下角坐标 [this.getX(), this.getY() + 60]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右下角坐标 [this.getX() + 40, this.getY() + 60]
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 40
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) { //左 右
                            //当前坦克 左下角坐标 [this.getX() , this.getY() + 60]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 右下角坐标 [this.getX() + 40, this.getY() + 60]
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 60
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                    }
                }
                break;
            case 3: //坦克面向 左
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i); //从Vector取出敌人坦克
                    if (enemyTank != this) { //不和自己比较, 对方坦克 面向 0=Up, 2=Down, 1=Left, 3=Right
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) { //上 下
                            //当前坦克 左上角坐标 [this.getX(), this.getY()]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 左下角坐标 [this.getX(), this.getY() + 40]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 60) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) { //左 右
                            //当前坦克 左上角坐标 [this.getX() , this.getY() + 60]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                            //当前坦克 左下角坐标 [this.getX(), this.getY() + 40]
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 40) {
                                return true; //在范围内 代表 已碰撞
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }
}
