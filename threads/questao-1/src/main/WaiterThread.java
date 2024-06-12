import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class WaiterThread extends Thread {
    private int idGarcom;
    private int capacity;
    private int currentCapacity = 0;

    public WaiterThread(int id, int WAITER_CAPACITY) {
        this.idGarcom = id;
        this.capacity = WAITER_CAPACITY;

    }

    @Override
    public void run() {

        List<Integer> idCliente = new ArrayList<>();

        while (Bar.barOpen) {
            try {
                Random random = new Random();

                // Pegar pedidos

                synchronized (Bar.Lock) {
                    if (!Bar.queueOrders.isEmpty()) {
                        int id = Bar.queueOrders.poll();
                        idCliente.add(id);
                        Bar.queueOrdersToPrepare.add(id);
                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " retirou pedido do cliente "
                                + (id + 1));
                        Thread.sleep(1000);
                        currentCapacity++;
                        if (currentCapacity == capacity) {
                            Bar.waitersAvailable.acquire();
                        }

                    }
                }

                if (idCliente.size() != 0 && (idCliente.size() == capacity || Bar.queueOrders.isEmpty())) {

                    Thread.sleep(2000);

                    for (Integer id : idCliente) {
                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " foi até a copa.");
                        // Entregar pedido
                        Thread.sleep((long) (2000)); // tempo para levar até o bartender
                        boolean prepared = false;
                        while (!prepared) {
                            try {
                                Bar.preparationBartender.acquire();
                            } finally {
                                new Thread(new BartenderThread(id)).start();
                                Thread.sleep(3000); // espera o preparo
                                prepared = true;
                                Bar.preparationBartender.release();
                            }
                        }

                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " está indo até o cliente " + (id + 1));

                        Thread.sleep((long) (random.nextInt(3) * 1000));

                        System.out
                                .println("\n\n  Garçom " + (idGarcom + 1) + " entregou pedido ao cliente " + (id + 1));

                        synchronized (Bar.Lock) {
                            Bar.queueOrdersToPrepare.remove(id);
                            Bar.customersServed++;
                        }

                    }

                    Thread.sleep(3000);// Pausa entre atendimentos
                    synchronized (Bar.Lock) {
                        Bar.waitersAvailable.release();
                        currentCapacity = 0;
                        idCliente.clear();
                    }
                    System.out.println("\n\n  Garçom " + (idGarcom + 1) + " está disponivel");

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
