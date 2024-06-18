import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    private static final Object lock = new Object();
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor ouvindo na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                new Thread(() -> {
                    boolean accepted;
                    synchronized (lock) {
                        accepted = !isBusy();
                        if (accepted) {
                            setBusy(true);
                        }
                    }

                    if (accepted) {
                        handleClient(clientSocket);
                        setBusy(false);
                    } else {
                        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                            out.println("Servidor está ocupado, tente novamente mais tarde.");
                        } catch (IOException e) {
                            System.err.println("Erro ao enviar mensagem de ocupado para o cliente: " + e.getMessage());
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                System.err.println("Erro ao fechar socket do cliente: " + e.getMessage());
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static boolean busy = false;

    private static synchronized boolean isBusy() {
        return busy;
    }

    private static synchronized void setBusy(boolean state) {
        busy = state;
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

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

            int sleepTime = new Random().nextInt(5000) + 1000;
            Thread.sleep(sleepTime);

            int result = performOperation(numbers, operation);
            out.println("Resultado: " + result);
            System.out.println("Operação: " + operation + ", Números: " + numbers + ", Resultado: " + result);

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao processar a conexão do cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar socket do cliente: " + e.getMessage());
            }
        }
    }

    private static int performOperation(List<Integer> numbers, String operation) {
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
                throw new UnsupportedOperationException("Operação desconhecida: " + operation);
        }
        return result;
    }
}
