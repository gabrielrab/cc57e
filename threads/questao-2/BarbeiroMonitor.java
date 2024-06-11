import java.util.Random;
import java.util.concurrent.locks.*;
import java.util.Queue;
import java.util.LinkedList;

public class BarbeiroMonitor {
    private int cadeirasLivres;
    private ReentrantLock lock = new ReentrantLock();
    private Condition clientesEsperando = lock.newCondition();
    private final int cadeiras;
    private Queue<Integer> filaClientes = new LinkedList<>();

    public BarbeiroMonitor(int cadeiras) {
        cadeirasLivres = cadeiras;
        this.cadeiras = cadeiras;
    }

    public boolean cortarCabelo(int clienteId) throws InterruptedException {
        lock.lock();

        try {
            if (cadeirasLivres == 0) {
                System.out.println("Cliente " + clienteId + " não encontrou cadeira livre. Saindo da barbearia...");
                return false;
            }

            cadeirasLivres--;
            filaClientes.add(clienteId);

            clientesEsperando.signal();

            System.out.println("Cliente " + clienteId + " sentou na cadeira.");

            clientesEsperando.await();

            cadeirasLivres++;

            System.out.println("Cliente " + clienteId + " saiu da barbearia.");
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void atenderCliente() throws InterruptedException {
        lock.lock();

        try {
            while (cadeirasLivres == cadeiras) {
                System.out.println("Barbeiro dormindo...");
                clientesEsperando.await();
            }

            Integer clienteId = filaClientes.poll();

            if (clienteId != null) {
                System.out.println("Barbeiro acordou e está cortando cabelo do cliente " + clienteId + "...");
                clientesEsperando.signal();

                // Tempo de corte de cabelo
                Thread.sleep(1000);

                System.out.println("Barbeiro terminou de cortar cabelo do cliente " + clienteId + ".");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        int cadeiras = 1;
        int clientes = 5;
        Random random = new Random();

        BarbeiroMonitor barbearia = new BarbeiroMonitor(cadeiras);
        
        for (int i = 0; i < clientes; i++) {
            int finalI = i;
            Thread cliente = new Thread(() -> {
                try {
                    Thread.sleep(random.nextInt(3000));
                    System.out.println("Cliente " + finalI + " chegou na barbearia.");
                    barbearia.cortarCabelo(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            cliente.start();
        }

        Thread barbeiro = new Thread(() -> {
            while (true) {
                try {
                    barbearia.atenderCliente();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        barbeiro.start();
    }
}
