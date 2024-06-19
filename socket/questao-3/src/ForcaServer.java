import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ForcaServer {
    private static final int PORT = 12345;
    private static final String[] WORDS = {"schutz"};
    private static final int MAX_ATTEMPTS = 6;
    private static Set<Character> guessedLetters = new HashSet<>();
    private static int incorrectGuesses = 0;
    private static String secretWord;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor do Jogo da Forca iniciado...");
            while (true) {
                secretWord = WORDS[(int) (Math.random() * WORDS.length)];
                guessedLetters.clear();
                incorrectGuesses = 0;

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

                out.println("Bem-vindo ao Jogo da Forca!");
                out.println("A palavra tem " + secretWord.length() + " letras.");
                out.println(getGameState());

                String request;
                while ((request = in.readLine()) != null) {
                    String response = handleRequest(request);
                    out.println(response);
                    if (response.startsWith("Você venceu") || response.startsWith("Você perdeu")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String handleRequest(String request) {
            if (request == null || request.length() != 1) {
                return "Comando inválido. Digite uma única letra.";
            }

            char guessedLetter = request.charAt(0);
            if (guessedLetters.contains(guessedLetter)) {
                return "Letra já adivinhada. Tente outra letra.\n" + getGameState();
            }

            guessedLetters.add(guessedLetter);
            if (secretWord.indexOf(guessedLetter) >= 0) {
                return "Letra correta!\n" + getGameState();
            } else {
                incorrectGuesses++;
                if (incorrectGuesses >= MAX_ATTEMPTS) {
                    return "Você perdeu! A palavra era: " + secretWord;
                }
                return "Letra incorreta. Você tem " + (MAX_ATTEMPTS - incorrectGuesses) + " tentativas restantes.\n" + getGameState();
            }
        }

        private String getGameState() {
            StringBuilder displayWord = new StringBuilder();
            for (char c : secretWord.toCharArray()) {
                if (guessedLetters.contains(c)) {
                    displayWord.append(c);
                } else {
                    displayWord.append('_');
                }
            }
            if (displayWord.indexOf("_") == -1) {
                return "Você venceu! A palavra era: " + secretWord;
            }
            return displayWord.toString() + "\n" + getHangmanState();
        }

        private String getHangmanState() {
            String[] hangmanStates = {
                    "",
                    " O ",
                    " O \n | ",
                    " O \n/| ",
                    " O \n/|\\",
                    " O \n/|\\\n/",
                    " O \n/|\\\n/ \\"
            };
            return hangmanStates[incorrectGuesses];
        }
    }
}
