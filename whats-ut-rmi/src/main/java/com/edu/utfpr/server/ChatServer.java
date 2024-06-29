package com.edu.utfpr.server;

import com.edu.utfpr.client.IChatClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

public class ChatServer extends UnicastRemoteObject implements IChatServer {
    private final Vector<Usuario> chatters;

    public ChatServer() throws RemoteException {
        super();
        chatters = new Vector<Usuario>(10, 1);
    }

    public static void main(String[] args) {
        startRMIRegistry();
        String hostName = "localhost";
        String serviceName = "GroupChatService";

        if (args.length == 2) {
            hostName = args[0];
            serviceName = args[1];
        }

        try {
            ChatServer hello = new ChatServer();
            Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
            System.out.println("Servidor RMI chat em grupo está executando...");
        } catch (Exception e) {
            System.out.println("Erro na inicialização do servidor");
        }
    }

    public static void startRMIRegistry() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("Servidor RMI pronto");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String sayHello(String ClientName) throws RemoteException {
        System.out.println(ClientName + " enviou uma mensagem");
        return "Olá " + ClientName + " do servidor de chat em grupo";
    }

    public void updateChat(String name, String nextPost) throws RemoteException {
        String message = name + " : " + nextPost + "\n";
        sendToAll(message);
    }

    @Override
    public void passIDentity(RemoteRef ref) throws RemoteException {
        try {
            System.out.println(ref.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerListener(String[] details) throws RemoteException {
        System.out.println(new Date(System.currentTimeMillis()));
        System.out.println(details[0] + " entrou na conversa");
        System.out.println(details[0] + "nome do host : " + details[1]);
        System.out.println(details[0] + "serviço RMI : " + details[2]);
        registerUsuario(details);
    }

    private void registerUsuario(String[] details) {
        try {
            IChatClient nextClient = (IChatClient) Naming.lookup("rmi://" + details[1] + "/" + details[2]);

            chatters.addElement(new Usuario(details[0], nextClient));

            nextClient.messageFromServer("[Servidor] : Olá " + details[0] + " agora você pode conversar.\n");
            sendToAll("[Servidor] : " + details[0] + " entrou no grupo.\n");

            updateUserList();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void updateUserList() {
        String[] currentUsers = getUserList();
        for (Usuario c : chatters) {
            try {
                c.getClient().updateUserList(currentUsers);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getUserList() {
        String[] allUsers = new String[chatters.size()];
        for (int i = 0; i < allUsers.length; i++) {
            allUsers[i] = chatters.elementAt(i).getName();
        }
        return allUsers;
    }

    public void sendToAll(String newMessage) {
        for (Usuario c : chatters) {
            try {
                c.getClient().messageFromServer(newMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void leaveChat(String userName) throws RemoteException {
        for (Usuario c : chatters) {
            if (c.getName().equals(userName)) {
                System.out.println(userName + " saiu da conversa");
                System.out.println(new Date(System.currentTimeMillis()));
                chatters.remove(c);
                break;
            }
        }
        if (!chatters.isEmpty()) {
            updateUserList();
        }
    }

    @Override
    public void sendPM(int[] privateGroup, String privateMessage) throws RemoteException {
        Usuario pc;
        for (int i : privateGroup) {
            pc = chatters.elementAt(i);
            pc.getClient().messageFromServer(privateMessage);
        }
    }
}

