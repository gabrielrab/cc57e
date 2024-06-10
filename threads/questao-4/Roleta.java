import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Roleta implements Runnable {

    private final int id;

    public Roleta(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Roleta " + id + " está girando.");
        try {
            Thread.sleep((long) (Math.random() * 1000)); // Simula o tempo de giro da roleta
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Roleta " + id + " parou.");
    }

    public static void main(String[] args) {
        int numRoletas = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(
            numRoletas
        );

        for (int i = 1; i <= numRoletas; i++) {
            executorService.execute(new Roleta(i));
        }

        executorService.shutdown();
    }
}
