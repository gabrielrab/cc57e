import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class BancoServer {
    private static final int PORT = 12345;
    private static Map<Integer, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        // Inicializa a conta com o número 123456 e saldo 50.0
        accounts.put(123456, new Account(123456, 50.0));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor da Instituição Financeira iniciado...");
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
        private PrintWriter out;
        private BufferedReader in;
        private Account currentAccount;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                out.println("Bem-vindo à Instituição Financeira!");
                out.println("Digite o número da sua conta:");
                int accountId = Integer.parseInt(in.readLine());

                synchronized (accounts) {
                    currentAccount = accounts.computeIfAbsent(accountId, id -> new Account(id, 0.0));
                }

                while (true) {
                    out.println("Escolha uma opção:\n1. Saldo\n2. Depósito\n3. Saque\n4. Sair");

                    String clientInput = in.readLine();
                    if (clientInput.equals("1")) {
                        out.println("Saldo atual: " + currentAccount.getBalance());
                    } else if (clientInput.equals("2")) {
                        out.println("Digite o valor a ser depositado:");
                        double depositAmount = Double.parseDouble(in.readLine());
                        currentAccount.deposit(depositAmount);
                        out.println("Depósito realizado com sucesso. Novo saldo: " + currentAccount.getBalance());
                    } else if (clientInput.equals("3")) {
                        out.println("Digite o valor a ser sacado:");
                        double withdrawalAmount = Double.parseDouble(in.readLine());
                        if (currentAccount.withdraw(withdrawalAmount)) {
                            out.println("Saque realizado com sucesso. Novo saldo: " + currentAccount.getBalance());
                        } else {
                            out.println("Saldo insuficiente.");
                        }
                    } else if (clientInput.equals("4")) {
                        out.println("Sessão encerrada.");
                        break;
                    } else {
                        out.println("Opção inválida. Tente novamente.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Account {
        private int id;
        private double balance;

        public Account(int id, double balance) {
            this.id = id;
            this.balance = balance;
        }

        public double getBalance() {
            return balance;
        }

        public synchronized void deposit(double amount) {
            balance += amount;
        }

        public synchronized boolean withdraw(double amount) {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }
    }
}
