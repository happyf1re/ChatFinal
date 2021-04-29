package server;

public class Hwtest {
    private boolean isa = false;
    private boolean isb = false;

    public static void main(String[] args) {
        Hwtest hwObject = new Hwtest();

        Thread t1 = new Thread(hwObject::printA);

        Thread t2 = new Thread(hwObject::printB);

        Thread t3 = new Thread(hwObject::printC);

        t1.start();
        t2.start();
        t3.start();

    }

    public synchronized void printA() {
        try {
            for (int i = 0; i < 5; i++) {
                while (isa) {
                    wait();
                }
                System.out.print('A');
                isa = true;
                isb = false;
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void printB() {
        try {
            for (int i = 0; i < 5; i++) {
                while (isb) {
                    wait();
                }
                System.out.print('B');
                isb = true;
                isa = true;
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void printC() {
        try {
            for (int i = 0; i < 5; i++) {
                while (!isb || !isa) {
                    wait();
                }
                System.out.print('C');
                isb = false;
                isa = false;
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

