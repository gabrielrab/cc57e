import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Loja implements Runnable {
    private final int id;
    private final Socket socket;

    public Loja(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            int numeroOperacoes = 1500;
            int milisegundos = 3000;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Random random = new Random();

            for (int i = 0; i < numeroOperacoes; i++) {
                String ocorrencia = "Filial " + id + ": " + (random.nextBoolean() ? "Compra" : "Venda") + " " + (i + 1);
                out.println(ocorrencia);

                try {
                    Thread.sleep(random.nextInt(milisegundos));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
