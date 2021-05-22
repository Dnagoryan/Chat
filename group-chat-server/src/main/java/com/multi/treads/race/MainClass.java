package com.multi.treads.race;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    private static CyclicBarrier cyclicBarrier;
    private static CountDownLatch countDownLatchStart;
    private static CountDownLatch countDownLatchFinish;
    private static Semaphore semaphore;
    public static final int CARS_COUNT = 4;
    private static boolean winner = false;
    private static String winnerName = "";

    public static boolean isWinner() {
        return winner;
    }

    public static void setWinner(boolean winner) {
        MainClass.winner = winner;
    }

    public static void setWinnerName(String winnerName) {
        MainClass.winnerName = winnerName;
    }

    public static String getWinnerName() {
        return winnerName;
    }

    public static CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public static CountDownLatch getCountDownLatchStart() {
        return countDownLatchStart;
    }

    public static CountDownLatch getCountDownLatchFinish() {
        return countDownLatchFinish;
    }

    public static Semaphore getSemaphore() {
        return semaphore;
    }

    public static void main(String[] args) throws  InterruptedException {

        countDownLatchFinish =new CountDownLatch(CARS_COUNT);
        countDownLatchStart=new CountDownLatch(CARS_COUNT);
        semaphore=new Semaphore(CARS_COUNT/2);
        cyclicBarrier=new CyclicBarrier(CARS_COUNT);

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        countDownLatchStart.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        countDownLatchFinish.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}


