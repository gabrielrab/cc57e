package com.edu.utfpr.client;

import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;
import com.edu.utfpr.server.IChatServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChatClient extends UnicastRemoteObject implements IChatClient {
    private final String hostName = "localhost";
    private final List<Consumer<String[]>> changeUserListListeners = new ArrayList<>();
    private final List<Consumer<String>> onReceiveMessageListeners = new ArrayList<>();
    private final String clientServiceName;
    protected IChatServer server;
    protected boolean isConnected = false;

    public ChatClient(String userName) throws RemoteException, MalformedURLException, NotBoundException, UserAlreadyRegisteredException {
        super();
        clientServiceName = "ClientListenService_" + userName;

        Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
        String serverServiceName = "GroupChatService";
        server = (IChatServer) Naming.lookup("rmi://" + hostName + "/" + serverServiceName);
    }

    public void register(String userName, String password) throws RemoteException, UserAlreadyRegisteredException, MalformedURLException, NotBoundException {
        server.registerUser(userName, password, hostName, clientServiceName);
    }

    public void login(String userName, String password) throws RemoteException, MalformedURLException, NotBoundException, InvalidUserOrPasswordException {
        server.login(userName, password, hostName, clientServiceName);
    }

    @Override
    public void receiveMessageFromServer(String message) throws RemoteException {
        for (Consumer<String> listener : onReceiveMessageListeners) {
            listener.accept(message);
        }
    }

    @Override
    public void updateUserList(String[] currentUsers) throws RemoteException {
        for (Consumer<String[]> listener : changeUserListListeners) {
            listener.accept(currentUsers);
        }
    }

    public void addChangeUserListListener(Consumer<String[]> listener) {
        changeUserListListeners.add(listener);
    }

    public void addOnReceiveMessageListener(Consumer<String> listener) {
        onReceiveMessageListeners.add(listener);
    }

}
