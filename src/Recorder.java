import java.io.*;
import java.util.Vector;

public class Recorder { // 记录相关信息 和文件交互
    private static int allEnemyTankNum = 0; // 击毁 敌人坦克的数量
    private static BufferedReader br = null;
    private static FileWriter fw = null; // 定义IO对象，用来保存数据到文件
    private static BufferedWriter bw = null;
    private static String recordFile = "src\\myRecord.txt";
    private static Vector<EnemyTank> enemyTanks = null;
    private static Vector<Node> nodes = new Vector<>();

    public static String getRecordFile() { // 得到文件路径
        return recordFile;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static Vector<Node> getNodes() { // 读取recordFile文件，恢复信息
        try {
            br = new BufferedReader(new FileReader(recordFile));
            allEnemyTankNum = Integer.parseInt(br.readLine()); // 得到 敌人坦克数量信息
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] xyd = line.split(" ");
                Node n = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]), Integer.parseInt(xyd[2]));
                nodes.add(n); // 封装每一格node 到Vector nodes里
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void keepRecord() { // 保存敌人坦克 坐标+方向
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(allEnemyTankNum + "\r\n");

            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank e = enemyTanks.get(i);
                if (e.isLive) {
                    // 如果 敌人坦克还存活，保存该信息
                    String record = e.getX() + " " + e.getY() + " " + e.getDirect();
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }

    public static void addAllEnemyTankNum() {
        Recorder.allEnemyTankNum++; // 击毁一个敌人坦克 就增加
    }
}
