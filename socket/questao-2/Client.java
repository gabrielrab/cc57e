import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                "Uso: java Client <numero1> <numero2> ... <operacao>"
            );
            return;
        }

        String host = "localhost";
        int port = 12345;

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            )
        ) {
            // Enviar números e operação para o servidor
            for (int i = 0; i < args.length - 1; i++) {
                out.println(args[i]);
            }
            out.println(args[args.length - 1]);

            String response = in.readLine();
            System.out.println("Resposta do servidor: " + response);
        } catch (IOException e) {
            System.err.println(
                "Erro ao comunicar com o servidor: " + e.getMessage()
            );
        }
    }
}
