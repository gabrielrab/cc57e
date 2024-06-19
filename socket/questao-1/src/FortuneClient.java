import java.io.*;
import java.net.*;

public class FortuneClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor de Fortunes.");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if ("LST-FORTUNE".equals(userInput)) {
                    String response;
                    while ((response = in.readLine()) != null && !response.isEmpty()) {
                        System.out.println(response);
                    }
                } else {
                    System.out.println("Resposta: " + in.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
