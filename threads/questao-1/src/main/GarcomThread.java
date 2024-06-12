import java.util.Random;

import model.GarsomClass;

import java.util.ArrayList;
import java.util.List;

class GarcomThread implements Runnable {
    private int idGarcom;
    private int capacity;

    public GarcomThread(int id, int CAPACIDADE_GARCONS) {
        this.idGarcom = id;
        this.capacity = CAPACIDADE_GARCONS;

    }

    @Override
    public void run() {
        List<Integer> idCliente = new ArrayList<>();
        while (Bar.barAberto) {
            try {
                Random random = new Random();
                GarsomClass current = Bar.garcoms.get(idGarcom);

                // Pegar pedidos

                synchronized (Bar.Lock) {
                    if (!Bar.filaPedidos.isEmpty()) {
                        int id = Bar.filaPedidos.poll();
                        idCliente.add(id);
                        Bar.filaPedidosPreparo.add(id);
                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " retirou pedido do cliente "
                                + (id + 1));
                        Thread.sleep(1000);
                        synchronized (current) {
                            current.currentCapacity++;
                            if (current.currentCapacity == capacity) {
                                Bar.garconsDisponiveis.acquire();
                            }
                        }
                    }
                }

                if (idCliente.size() != 0  && (idCliente.size() == capacity || Bar.filaPedidos.isEmpty())) {

                    Thread.sleep(2000);

                    for (Integer id : idCliente) {
                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " foi até a copa.");
                        // Entregar pedido
                        Thread.sleep((long) (2000)); // tempo para levar até o bartender
                        boolean prepared = false;
                        while (!prepared) {
                            try{
                                Bar.preparoBartender.acquire();
                            }
                            finally{
                                new Thread(new BartenderThread(id)).start();
                                Thread.sleep(3000);
                                prepared = true;
                                Bar.preparoBartender.release();
                            }
                        }

                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " está indo até o cliente " + (id + 1));

                        Thread.sleep((long) (random.nextInt(3) * 1000));

                        System.out.println("\n\n  Garçom " + (idGarcom + 1) + " entregou pedido ao cliente " + (id + 1));

                        synchronized (Bar.Lock) {
                            Bar.filaPedidosPreparo.remove(id);
                            Bar.clientesAtendidos++;
                        }

                    }

                    Thread.sleep(3000);// Pausa entre atendimentos
                    synchronized (Bar.Lock) {
                        Bar.garconsDisponiveis.release();
                        current.isAvailable = true;
                        current.currentCapacity = 0;
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
