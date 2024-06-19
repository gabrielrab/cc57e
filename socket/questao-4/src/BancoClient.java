import java.io.*;
import java.net.*;

public class BancoClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor da Instituição Financeira.");
            System.out.println(in.readLine()); // Bem-vindo
            System.out.println(in.readLine()); // Digite o número da sua conta

            System.out.print("Digite o número da sua conta: ");
            String accountNumber = stdIn.readLine();
            out.println(accountNumber);

            String serverResponse;
            while (true) {
                serverResponse = in.readLine();
                System.out.println("Servidor: " + serverResponse);
                if (serverResponse.equals("Sessão encerrada.")) {
                    break;
                }

                if (serverResponse.startsWith("Escolha uma opção:")) {
                    System.out.print("Escolha uma opção: ");
                    String opcao = stdIn.readLine();
                    out.println(opcao);

                    serverResponse = in.readLine();
                    System.out.println("Servidor: " + serverResponse);

                    if (opcao.equals("2") || opcao.equals("3")) {
                        System.out.print("Digite o valor: ");
                        String valor = stdIn.readLine();
                        out.println(valor);
                        serverResponse = in.readLine();
                        System.out.println("Servidor: " + serverResponse);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
