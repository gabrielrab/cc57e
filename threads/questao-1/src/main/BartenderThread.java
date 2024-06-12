public class BartenderThread implements Runnable {

    private Integer idCliente;

    public BartenderThread(int id) {
        this.idCliente = id;
    }

    @Override
    public void run() {
        try {
            // Prepara pedido
            Thread.sleep(1000); // tempo para entregar o pedido
            System.out.println("\n\n       Bartender está preparando o pedido do cliente " + (idCliente + 1));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}
