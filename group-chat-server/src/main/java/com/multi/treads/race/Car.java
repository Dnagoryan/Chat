package com.multi.treads.race;


import java.util.concurrent.BrokenBarrierException;

import static com.multi.treads.race.MainClass.*;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            getCyclicBarrier().await();
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            getCountDownLatchStart().countDown();
            getCyclicBarrier().await();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getCyclicBarrier().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (!isWinner()) {
            setWinnerName(this.name);
            setWinner(true);
            System.out.println("\nПобедитель гонки: "+ getWinnerName()+"\n");
        }

        getCountDownLatchFinish().countDown();
    }

}


