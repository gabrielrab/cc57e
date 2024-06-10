import java.util.LinkedList;
import java.util.Queue;

class BufferMonitor {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public BufferMonitor(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produzir(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
        System.out.println("Produziu: " + item);
        notifyAll();
    }

    public synchronized int consumir() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        int item = queue.poll();
        System.out.println("Consumiu: " + item);
        notifyAll();
        return item;
    }
}

public class Monitor {

    public static void main(String[] args) {
        BufferMonitor buffer = new BufferMonitor(5);

        Runnable produtor = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    buffer.produzir(i);
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumidor = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    buffer.consumir();
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread produtorThread = new Thread(produtor);
        Thread consumidorThread = new Thread(consumidor);

        produtorThread.start();
        consumidorThread.start();
    }
}
