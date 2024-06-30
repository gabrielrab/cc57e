package com.edu.utfpr.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends Remote {
    void receiveMessageFromServer(String message) throws RemoteException;

    void updateUserList(String[] currentUsers) throws RemoteException;
}
