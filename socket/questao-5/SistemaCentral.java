import java.io.*;
import java.net.*;

public class SistemaCentral {
        public static void main(String[] args) throws IOException {
            int numeroDeLojas = 5;

            ServerSocket serverSocket = new ServerSocket(12345);

            for (int i = 0; i < numeroDeLojas; i++) {
                new Thread(new Loja(i, new Socket("localhost", 12345))).start();
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            System.out.println(inputLine);
                        }

                        in.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }

}

