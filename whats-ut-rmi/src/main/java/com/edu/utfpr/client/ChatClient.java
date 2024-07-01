package com.edu.utfpr.client;

import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.Messages;
import com.edu.utfpr.core.entities.User;
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
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.JPanel;

public class ChatClient extends UnicastRemoteObject implements IChatClient {
    private final String hostName = "localhost";
    private final List<Consumer<List<User>>> changeUserListListeners = new ArrayList<>();
    private final List<Consumer<List<Chat>>> changeGroupListListeners = new ArrayList<>();
    private final List<Consumer<List<Chat>>> changeMyChatsListListeners = new ArrayList<>();
    private final List<Consumer<Chat>> changeCurrentChatListeners = new ArrayList<>();
    private final List<Consumer<Messages>> onReceiveMessageListeners = new ArrayList<>();
    private final String clientServiceName;
    public Chat currentChat;
    public String userName;
    protected IChatServer server;

    public ChatClient(String userName)
            throws RemoteException, MalformedURLException, NotBoundException {
        super();
        clientServiceName = "ClientListenService_" + userName;

        Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
        String serverServiceName = "GroupChatService";
        server = (IChatServer) Naming.lookup("rmi://" + hostName + "/" + serverServiceName);
    }

    public void register(String userName, String password)
            throws RemoteException, UserAlreadyRegisteredException {
        server.registerUser(userName, password);
    }

    public void login(String userName, String password)
            throws RemoteException, MalformedURLException, NotBoundException, InvalidUserOrPasswordException {
        server.login(userName, password, hostName, clientServiceName);
        this.userName = userName;
    }

    public List<User> getCurrentUsers() throws RemoteException {
        return server.getCurrentUsers();
    }

    public List<Chat> getAllGroups() throws RemoteException {
        return server.getAllGroups();
    }

    public List<Chat> getMyChats() throws RemoteException {
        return server.getMyChats(userName);
    }

    public void createPrivateChat(String destinationUser) throws RemoteException {
        server.createPrivateChat(userName, destinationUser);
    }

    public void createChatGroup(String chatName, String user, Boolean exitAdminMethod) throws RemoteException {
        server.createChatGroup(chatName, user, exitAdminMethod);
    }

    @Override
    public void updateUserList(List<User> currentUsers) throws RemoteException {
        for (Consumer<List<User>> listener : changeUserListListeners) {
            listener.accept(currentUsers);
        }
    }

    public void addChangeCurrentChatListener(Consumer<Chat> listener) {
        changeCurrentChatListeners.add(listener);
    }

    public void addChangeUserListListener(Consumer<List<User>> listener) {
        changeUserListListeners.add(listener);
    }

    public void addChangeGroupListListener(Consumer<List<Chat>> listener) {
        changeGroupListListeners.add(listener);
    }

    public void addChangeMyChatsListListener(Consumer<List<Chat>> listener) {
        changeMyChatsListListeners.add(listener);
    }

    public void addOnReceiveMessageListener(Consumer<Messages> listener) {
        onReceiveMessageListeners.add(listener);
    }

    public void setCurrentChat(Chat chat) throws RemoteException {
        currentChat = chat;
        for (Consumer<Chat> listener : changeCurrentChatListeners) {
            listener.accept(chat);
        }
    }

    public void sendMessage(String message, JPanel inputPanel) throws RemoteException {
        server.sendMessage(userName, currentChat, message, inputPanel);
    }

    @Override
    public void updatePublicGroupList(List<Chat> groups) throws RemoteException {
        for (Consumer<List<Chat>> listener : changeGroupListListeners) {
            listener.accept(groups);
        }
    }

    @Override
    public void updateChatList(List<Chat> myChats) throws RemoteException {
        for (Consumer<List<Chat>> listener : changeMyChatsListListeners) {
            listener.accept(myChats);
        }
    }

    @Override
    public void receiveMessage(Messages message) throws RemoteException {
        currentChat.messages.add(message);
        for (Consumer<Messages> listener : onReceiveMessageListeners) {
            listener.accept(message);
        }
    }

    @Override
    public UUID getCurrentChatId() throws RemoteException {
        return currentChat.chatId;
    }

    @Override
    public void sendInviteAdmin(String userName, Chat chat) throws RemoteException{
        server.createInviteGroup(userName, chat);
    }

}
