package com.ABC.tread.monitor;

public class TreadControl {
    private static volatile char nextChar = 'A';
    private static Object monitor = new Object();

    public static void main(String[] args)  {
        TreadControl treadControl = new TreadControl();
        Thread threadA = new Thread(() -> treadControl.methodA());
        Thread threadB = new Thread(() -> treadControl.methodB());
        Thread threadC = new Thread(() -> treadControl.methodC());
        threadA.start();
        threadB.start();
        threadC.start();
    }

    private void methodA() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while ('A' != nextChar) {
                        monitor.wait();
                    }

                    System.out.print("\n"+nextChar);
                    nextChar='B';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void methodB() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while ('B' != nextChar) {
                        monitor.wait();
                    }
                    System.out.print(nextChar);
                    nextChar='C';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void methodC() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while ('C' != nextChar) {
                        monitor.wait();
                    }
                    System.out.print(nextChar);
                    nextChar='A';
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

