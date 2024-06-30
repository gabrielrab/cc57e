package com.edu.utfpr.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.Messages;
import com.edu.utfpr.core.entities.User;

public interface IChatClient extends Remote {
    UUID getCurrentChatId() throws RemoteException;

    void updateUserList(List<User> currentUsers) throws RemoteException;

    void updatePublicGroupList(List<Chat> groups) throws RemoteException;

    void updateChatList(List<Chat> MyChats) throws RemoteException;

    void updateCurrentChat(Chat chat) throws RemoteException;

    void receiveMessage(Messages message) throws RemoteException;
}
