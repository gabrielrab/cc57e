import java.util.Random;

class ClienteThread implements Runnable {
    private int idCliente;
    Random random = new Random();

    public ClienteThread(int id) {
        this.idCliente = id;
    }

    @Override
    public void run() {

        try {
            Thread.sleep((long) (random.nextInt(6) * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.err.println("\nCliente " + (idCliente + 1) + " entrou no restaurante.");

        while (Bar.barAberto) {
            Random random2 = new Random();
            try {

                Thread.sleep((long) (random2.nextInt(6) * 1000));

                int availablePermits = Bar.garconsDisponiveis.availablePermits();
                if (availablePermits != 0) {
                    // Fazer pedido
                    boolean exist = Bar.filaPedidos.contains(idCliente);
                    if (exist == false) {
                        synchronized (Bar.Lock) {
                            Bar.filaPedidos.add(idCliente);
                            System.out.println(
                                    "\n\nCliente " + (idCliente + 1) + " chamou um garçom para realizar um pedido.");
                        }
                        System.out.println("\n\nCliente " + (idCliente + 1) + " está esperando por garçons");

                        while (Bar.filaPedidos.contains(idCliente)) {
                            if (!Bar.filaPedidos.contains(idCliente)) {
                                break;
                            }
                            Thread.sleep(1000);
                        }

                        System.out.println("\n\nCliente " + (idCliente + 1) + " está esperando seu pedido.");

                        while (Bar.filaPedidosPreparo.contains(idCliente)) {
                            if (!Bar.filaPedidosPreparo.contains(idCliente)) {
                                break;
                            }
                            Thread.sleep(1000);
                        }
                        // Consumir pedido
                        Thread.sleep(1000);
                        System.out.println("\n\nCliente " + (idCliente + 1) + " está bebendo o drink.");
                        Thread.sleep((long) (random2.nextInt(6) * 1000)); // tempo variável para consumir o pedido
                        System.out.println("\n\nCliente " + (idCliente + 1) + " terminou de beber.");
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
