public class roundController extends Thread {

    @Override
    public void run() {

        for (int i = 0; i < Bar.NUM_ROUNDS; i++) {
            try {
                System.err.println("\n\n_________Rodada " + (i + 1) + "__________");
                Thread.sleep(20000);
                System.err.println("\n\n_________Rodada " + (i + 1) + " Finalizada__________");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bar.round++;
        }
        Bar.barOpen = false;

        System.out.println("\n Pedidos entregues: " + Bar.customersServed);
        System.out.println("\n Bar fechado, finalizando o serviço");
        System.exit(0);
    }

}
