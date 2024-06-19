import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FortuneServer {
    private static final int PORT = 12345;
    private static List<String> fortunes = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de Fortunes iniciado...");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    String response = handleRequest(request);
                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String handleRequest(String request) {
            if (request == null) {
                return "Comando inválido";
            }

            if (request.startsWith("GET-FORTUNE")) {
                return getFortune();
            } else if (request.startsWith("ADD-FORTUNE ")) {
                return addFortune(request.substring(12).trim());
            } else if (request.startsWith("UPD-FORTUNE ")) {
                String[] parts = request.split(" ", 3);
                if (parts.length < 3) {
                    return "Formato de comando inválido";
                }
                try {
                    int index = Integer.parseInt(parts[1]);
                    return updateFortune(index, parts[2]);
                } catch (NumberFormatException e) {
                    return "Formato de índice inválido";
                }
            } else if (request.startsWith("LST-FORTUNE")) {
                return listFortunes();
            }
            return "Comando inválido";
        }

        private String getFortune() {
            if (fortunes.isEmpty()) {
                return "Nenhuma frase disponível.";
            }
            Random rand = new Random();
            return fortunes.get(rand.nextInt(fortunes.size()));
        }

        private String addFortune(String fortune) {
            fortunes.add(fortune);
            return "Frase adicionada.";
        }

        private String updateFortune(int index, String fortune) {
            if (index < 0 || index >= fortunes.size()) {
                return "Índice inválido.";
            }
            fortunes.set(index, fortune);
            return "Frase atualizada.";
        }

        private String listFortunes() {
            if (fortunes.isEmpty()) {
                return "Nenhuma frase disponível.";
            }
            StringBuilder response = new StringBuilder();
            for (String fortune : fortunes) {
                response.append(fortune).append("\n");
            }
            return response.toString().trim();
        }
    }
}
