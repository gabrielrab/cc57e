import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Bar {

    // Parâmetros
    public static final int NUM_CLIENTS = 3; // a quantidade de clientes presentes no estabelecimento
    public static final int NUM_WAITER = 2; // a quantidade de garçons que estão trabalhando
    public static final int WAITER_CAPACITY = 2; // a capacidade de atendimento dos garçons
    public static final int NUM_ROUNDS = 2; // o número de rounds que serão liberadas no bar

    // Variáveis
    public static final Semaphore waitersAvailable = new Semaphore(NUM_WAITER);
    public static final Semaphore preparationBartender = new Semaphore(1);

    public static final Queue<Integer> queueOrders = new LinkedList<>();
    public static final Queue<Integer> queueOrdersToPrepare = new LinkedList<>();

    public static final Object Lock = new Object();

    public static boolean barOpen = true;
    public static int customersServed = 0;

    public static int round = 1;

    public static void main(final String[] args) {

        for (int i = 0; i < NUM_WAITER; i++) {
            System.err.println("\n  Garçom " + (i + 1) + " está aguardando no salão.");
            new Thread(new WaiterThread(i, WAITER_CAPACITY)).start();
        }

        new Thread(new roundController()).start();

        for (int i = 0; i < NUM_CLIENTS; i++) {
            new Thread(new ClientThread(i)).start();
        }
    }
}