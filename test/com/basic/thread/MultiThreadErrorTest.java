package com.basic.thread;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadErrorTest {
    volatile Integer mIndex = 0;
    boolean[] mFlag = new boolean[20000];
    AtomicInteger realIndex = new AtomicInteger();
    AtomicInteger wrongCount = new AtomicInteger();


    @Test
    public void testPlusPlus() {
        Thread thread1 = new Thread(new APlusPlus());
        Thread thread2 = new Thread(new APlusPlus());
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(mIndex);
        System.out.println(realIndex.get());
        System.out.println(wrongCount.get());

    }

    private class APlusPlus implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                mIndex++;
                if (mFlag[mIndex]) {
                    wrongCount.incrementAndGet();
                }
                mFlag[mIndex] = true;
                realIndex.incrementAndGet();
            }
        }
    }
}
