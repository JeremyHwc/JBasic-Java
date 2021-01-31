package com.basic.thread;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.*;

public class ThreadTest {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    /**
     * 在不使用setDaemon方法设置线程是否是守护线程时，子线程的Daemon属性与父线程的Daemon属性相同。
     *
     * <result>
     * name: parent priority: 5 and daemon: true
     * name: child priority: 5 and daemon: true
     * <result/>
     */
    @Test
    public void testThreadPriority() {
        Thread parent = new Thread("parent") {
            @Override
            public void run() {
                System.out.println("name: " + getName() + " priority: " + getPriority() + " and daemon: "
                        + isDaemon());

                Thread child = new Thread("child") {
                    @Override
                    public void run() {
                        System.out.println("name: " + getName() + " priority: " + getPriority() + " and daemon: "
                                + isDaemon());
                    }
                };
                child.start();
            }
        };
        parent.setDaemon(true);
        parent.start();
    }

    /**
     * <result>
     * time: Sun Jan 31 11:58:54 CST 2021 threadName: main
     * main thread state: WAITING
     * time: Sun Jan 31 11:58:54 CST 2021 threadName: Thread-0
     * time: Sun Jan 31 11:59:04 CST 2021 threadName: Thread-0
     * time: Sun Jan 31 11:59:04 CST 2021 threadName: main<result/>
     */
    @Test
    public void testJoin() {
        Thread mainThread = Thread.currentThread();
        System.out.println("time: " + new Date() + " threadName: " + mainThread.getName());
        Thread childThread = new Thread(() -> {
            try {
                System.out.println("main thread state: " + mainThread.getState());
                System.out.println("time: " + new Date() + " threadName: " + Thread.currentThread().getName());
                Thread.sleep(10000L);
                System.out.println("time: " + new Date() + " threadName: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            // 开一个子线程去执行
            childThread.start();
            // 当前主线程等待子线程执行完成之后再执行
            childThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("time: " + new Date() + " threadName: " + Thread.currentThread().getName());
    }

    /**
     * <result>
     * child begin...
     * child was interrupted.
     * <result/>
     */
    @Test
    public void testInterrupt() {
        Thread child = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " begin...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted.");
            }
        }, "child");
        child.start();
        try {
            Thread.sleep(1000);
            if (child.isAlive()) {
                child.interrupt();
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }
    }

    /**
     * <result>
     * Product made in child thread.
     * </result>
     */
    @Test
    public void testThreadWithReturnValue() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        FutureTask<String> futureTask = new FutureTask<>(() -> "Product made in child thread.");
        threadPoolExecutor.submit(futureTask);
        try {
            String childResult = futureTask.get();
            System.out.println(childResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeadLock() throws InterruptedException {
        //初始化线程1，线程1需要在锁定lock1共享资源的情况下再锁定lock2
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " begin.");
                // 这里sleep 2s，是为了确保线程拿到lock1，避免其中一个线程执行太快，另外一个线程还没开始执行，直接执行完毕，没有发生死锁的现象
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName());
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        });

        //初始化线程2，线程1需要在锁定lock2共享资源的情况下再锁定lock1
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " begin.");
                // 这里sleep 2s，是为了确保线程拿到lock2，避免其中一个线程执行太快，另外一个线程还没开始执行，直接执行完毕，没有发生死锁的现象
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName());
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        });
        thread1.start();
        thread2.start();
    }
}
