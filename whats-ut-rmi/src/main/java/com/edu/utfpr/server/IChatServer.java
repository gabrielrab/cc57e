package com.edu.utfpr.server;

import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.User;
import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatServer extends Remote {

        void createChatGroup(String chatName, String creator, boolean exitAdminMethod)
                        throws RemoteException;

        void createPrivateChat(String user1, String user2) throws RemoteException;

        void sendMessage(String user, Chat chat, String message) throws RemoteException;

        void leaveGroup(User user, Chat chat) throws RemoteException;

        void createInviteGroup(User user, Chat chat) throws RemoteException;

        void acceptInviteGroup(User user, Chat chat) throws RemoteException;

        List<User> getPendingUsersGroup(Chat chat) throws RemoteException;

        List<User> getMembersGroup(Chat chat) throws RemoteException;

        void registerUser(String userName, String password, String hostName, String clientServiceName)
                        throws RemoteException, UserAlreadyRegisteredException, MalformedURLException,
                        NotBoundException;

        void login(String userName, String password, String hostName, String clientServiceName)
                        throws RemoteException, MalformedURLException, NotBoundException,
                        InvalidUserOrPasswordException;

        List<User> getCurrentUsers() throws RemoteException;

        List<Chat> getAllGroups() throws RemoteException;

        List<Chat> getMyChats(String userName) throws RemoteException;
}
