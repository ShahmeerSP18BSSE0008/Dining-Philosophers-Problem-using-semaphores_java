import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Dining_Philosophers {

    static int philosophersCount = 5;
    static Philosopher philosophers[] = new Philosopher[philosophersCount];
    static Fork forks[] = new Fork[philosophersCount];

    static class Fork {

        public Semaphore mutex = new Semaphore(1);

        void grab() {
            try {
                mutex.acquire();
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        void release() {
            mutex.release();
        }

        boolean isFree() {
            return mutex.availablePermits() > 0;
        }

    }

    static class Philosopher extends Thread {

        public int number;
        public Fork leftFork;
        public Fork rightFork;

        Philosopher(int num, Fork left, Fork right) {
            number = num;
            leftFork = left;
            rightFork = right;
        }

        public void run(){
            System.out.println("Hi! I'm philosopher No. " + number);

            while (true) {
                leftFork.grab();
                System.out.println("Philosopher No. " + number + " grabs left fork.");
                rightFork.grab();
                System.out.println("Philosopher No. " + number + " grabs right fork.");
                eat();
                leftFork.release();
                System.out.println("Philosopher No. " + number + " releases left fork.");
                rightFork.release();
                System.out.println("Philosopher No. " + number + " releases right fork.");
            }
        }

        void eat() {
            try {
                int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);
                System.out.println("Philosopher No. " + number + " eats for " + sleepTime);
                Thread.sleep(sleepTime);
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

    }

    public static void main(String argv[]) {
        System.out.println("Dining philosophers problem.");

        for (int i = 0; i < philosophersCount; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < philosophersCount; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % philosophersCount]);
            philosophers[i].start();
        }

        while (true) {
            try {
                // sleep 1 sec
                Thread.sleep(1000);

                // check for deadlock
                boolean deadlock = true;
                for (Fork f : forks) {
                    if (f.isFree()) {
                        deadlock = false;
                        break;
                    }
                }
                if (deadlock) {
                    Thread.sleep(1000);
                    System.out.println("There is a deadlock!");
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        System.out.println("Bye!");
        System.exit(0);
    }

}
