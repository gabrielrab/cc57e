import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        int port = 12345; // Porta para o servidor ouvir

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor ouvindo na porta " + port);

            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                    );
                    PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(),
                        true
                    )
                ) {
                    System.out.println("Cliente conectado");

                    List<Integer> numbers = new ArrayList<>();
                    String line;
                    String operation = "";

                    while ((line = in.readLine()) != null) {
                        try {
                            numbers.add(Integer.parseInt(line));
                        } catch (NumberFormatException e) {
                            operation = line;
                            break;
                        }
                    }

                    int result = performOperation(numbers, operation);
                    out.println("Resultado: " + result);
                } catch (IOException e) {
                    System.err.println(
                        "Erro ao comunicar com o cliente: " + e.getMessage()
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static int performOperation(
        List<Integer> numbers,
        String operation
    ) {
        int result = 0;
        switch (operation) {
            case "SUM":
                for (int number : numbers) {
                    result += number;
                }
                break;
            case "MUL":
                result = 1;
                for (int number : numbers) {
                    result *= number;
                }
                break;
            default:
                throw new UnsupportedOperationException(
                    "Operação desconhecida: " + operation
                );
        }
        return result;
    }
}
