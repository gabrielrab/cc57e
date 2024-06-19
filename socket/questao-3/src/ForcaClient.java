import java.io.*;
import java.net.*;

public class ForcaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor do Jogo da Forca.");
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println("Servidor: " + serverResponse);
                if (serverResponse.startsWith("Você venceu") || serverResponse.startsWith("Você perdeu")) {
                    break;
                }
                System.out.print("Digite uma letra: ");
                String userInput = stdIn.readLine();
                if (userInput != null && userInput.length() == 1 && Character.isLetter(userInput.charAt(0))) {
                    out.println(userInput);
                } else {
                    System.out.println("Entrada inválida. Digite uma única letra.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
