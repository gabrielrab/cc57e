package com.edu.utfpr.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface IChatServer extends Remote {
    void updateChat(String userName, String chatMessage) throws RemoteException;
    void passIDentity(RemoteRef ref) throws RemoteException;
    void registerListener(String[] details) throws RemoteException;
    void leaveChat(String userName) throws RemoteException;
    void sendPM(int[] privateGroup, String privateMessage) throws RemoteException;
}
