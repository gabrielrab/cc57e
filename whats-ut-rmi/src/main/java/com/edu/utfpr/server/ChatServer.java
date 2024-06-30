package com.edu.utfpr.server;

import com.edu.utfpr.client.IChatClient;
import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChatServer extends UnicastRemoteObject implements IChatServer {
    private final Vector<Usuario> users;
    private final Map<String, String> userCredentials = new HashMap<>();

    public ChatServer() throws RemoteException {
        super();
        users = new Vector<>(10, 1);
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
            System.out.println("Servidor RMI WhatsUI executando...");
        } catch (Exception e) {
            System.out.println("Erro na inicialização do servidor");
        }
    }

    public static void startRMIRegistry() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("Servidor RMI inicializado");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateChat(String name, String nextPost) throws RemoteException {
        String message = name + " : " + nextPost + "\n";
        sendToAll(message);
    }

    @Override
    public void login(String userName, String password, String hostName, String clientServiceName) throws RemoteException, MalformedURLException, NotBoundException, InvalidUserOrPasswordException {
        if (userCredentials.containsKey(userName) && userCredentials.get(userName).equals(password)) {
            IChatClient nextClient = (IChatClient) Naming.lookup("rmi://" + hostName + "/" + clientServiceName);
            users.addElement(new Usuario(userName, nextClient));
            sendToAll("[Servidor] : " + userName + " entrou no grupo.\n");
            updateUserList();
        } else {
            throw new InvalidUserOrPasswordException();
        }
    }

    @Override
    public void registerUser(String userName, String password, String hostName, String clientServiceName) throws RemoteException, UserAlreadyRegisteredException, MalformedURLException, NotBoundException {
        if (userCredentials.containsKey(userName)) {
            throw new UserAlreadyRegisteredException();
        }

        userCredentials.put(userName, password);

        IChatClient nextClient = (IChatClient) Naming.lookup("rmi://" + hostName + "/" + clientServiceName);
        users.addElement(new Usuario(userName, nextClient));
        sendToAll("[Servidor] : " + userName + " entrou no grupo.\n");
        updateUserList();
    }

    private void updateUserList() {
        String[] currentUsers = getUserList();
        for (Usuario c : users) {
            try {
                c.getClient().updateUserList(currentUsers);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getUserList() {
        String[] allUsers = new String[users.size()];
        for (int i = 0; i < allUsers.length; i++) {
            allUsers[i] = users.elementAt(i).getName();
        }
        return allUsers;
    }

    public void sendToAll(String newMessage) {
        for (Usuario c : users) {
            try {
                c.getClient().receiveMessageFromServer(newMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void leaveChat(String userName) throws RemoteException {
        for (Usuario c : users) {
            if (c.getName().equals(userName)) {
                System.out.println(userName + " saiu da conversa");
                System.out.println(new Date(System.currentTimeMillis()));
                users.remove(c);
                break;
            }
        }
        if (!users.isEmpty()) {
            updateUserList();
        }
    }

    @Override
    public void sendPM(int[] privateGroup, String privateMessage) throws RemoteException {
        Usuario pc;
        for (int i : privateGroup) {
            pc = users.elementAt(i);
            pc.getClient().receiveMessageFromServer(privateMessage);
        }
    }
}

