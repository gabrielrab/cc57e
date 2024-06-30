package com.edu.utfpr.server;

import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatServer extends Remote {
    void updateChat(String userName, String chatMessage) throws RemoteException;

    void registerUser(String userName, String password, String hostName, String clientServiceName) throws RemoteException, UserAlreadyRegisteredException, MalformedURLException, NotBoundException;

    void login(String userName, String password, String hostName, String clientServiceName) throws RemoteException, MalformedURLException, NotBoundException, InvalidUserOrPasswordException;

    void leaveChat(String userName) throws RemoteException;

    void sendPM(int[] privateGroup, String privateMessage) throws RemoteException;
}
