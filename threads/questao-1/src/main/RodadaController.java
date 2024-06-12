public class RodadaController implements Runnable {

    @Override
    public void run() {
       
        
        for (int i = 0; i < Bar.NUM_RODADAS; i++) {
                try {
                    System.err.println("\n\n_________Rodada "+(i+1)+"__________");
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bar.rodada++;
        }
            Bar.barAberto = false;
        
        
        

        System.out.println("\n Pedidos entregues: " + Bar.clientesAtendidos);
        System.out.println("\n Bar fechado, finalizando o serviço");
        System.exit(0);
    }
    
}
