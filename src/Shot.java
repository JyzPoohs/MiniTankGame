public class Shot implements Runnable {
    int x, y, direct = 0;
    int speed = 2; // 子弹速度
    boolean isLive = true;

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (isLive) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            switch (direct) { // 改变子弹方向
                case 0: // up
                    y -= speed;
                    break;
                case 1: // Right
                    x += speed;
                    break;
                case 2: // Down
                    y += speed;
                    break;
                case 3: // Left
                    x -= speed;
                    break;
            }
            // 画板大小 1000*750，不在范围里==碰到边界 就销毁
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}
