class RunnableDemo implements Runnable {
    private Thread t;
    private String threadName;

    RunnableDemo( String name) {
        threadName = name;
    }

    public void run() {
        System.out.println("Running " +  threadName );
        try {
            // 让线程睡眠一会
            Thread.sleep(50);
        }catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}

class ThreadDemo extends Thread{
    @Override
    public void run() {
        System.out.println("ThreadDemo class run()");
    }
}

class RunnableThread implements Runnable{

    @Override
    public void run() {
        System.out.println("RunnableThread run()");
    }
}

public class TestThread {

    public static void main(String args[]) {
        RunnableDemo R1 = new RunnableDemo( "Thread-1");
        R1.start();

        ThreadDemo t = new ThreadDemo();
        t.start();

        RunnableThread r = new RunnableThread();
        new Thread(r).start();

    }
}