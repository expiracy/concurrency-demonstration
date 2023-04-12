public class Counter {
    private int val = 0;

    public static void main(String[] args) {
        Counter counter = new Counter();

        int numberOfThreads = 10;

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(new CounterModifier(counter)).start();
        }
    }

    public void increment() {
        this.val++;
    }

    public void decrement() {
        this.val--;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
}

class CounterIncrementer implements Runnable {
    private Counter counter;

    public CounterIncrementer(Counter counter) {
        this.counter = counter;
    }

    public void increment() {
        synchronized (this.counter) {
            System.out.println(Thread.currentThread().getName() + ": " + this.counter.getVal());
            this.counter.increment();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.increment();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class CounterDecrementer implements Runnable {
    private Counter counter;

    public CounterDecrementer(Counter counter) {
        this.counter = counter;
    }

    public void decrement() {
        synchronized (this.counter) {
            System.out.println(Thread.currentThread().getName() + ": " + this.counter.getVal());
            this.counter.decrement();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.decrement();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class CounterModifier implements Runnable {
    private Counter counter;

    public CounterModifier(Counter counter) {
        this.counter = counter;
    }

    /**
     * Method demonstrates how a synchronized block allows the atomic action performed on the shared resource to be defined
     */
    public void modify() throws InterruptedException {
        synchronized (this.counter) {
            System.out.println(Thread.currentThread().getName() + ": " + this.counter.getVal());

            // This demonstrates a "check then act" race condition
            if (this.counter.getVal() < 1000) {
                this.counter.setVal(this.counter.getVal() + 100);
            }
            Thread.sleep(500);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.modify();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


