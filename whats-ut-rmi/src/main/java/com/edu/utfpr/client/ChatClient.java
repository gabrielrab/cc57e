package com.edu.utfpr.client;

import com.edu.utfpr.server.IChatServer;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements IChatClient{
    ChatClientGUI chatGUI;
    private final String clientServiceName;
    private final String name;
    protected IChatServer server;
    protected boolean isConnected = false;

    public ChatClient(ChatClientGUI aChatGUI, String userName) throws RemoteException {
        super();
        this.chatGUI = aChatGUI;
        this.name = userName;
        this.clientServiceName = "ClientListenService_" + userName;
    }

    public void startClient() throws RemoteException {
        String hostName = "localhost";
        String[] details = {name, hostName, clientServiceName};

        try {
            Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
            String serviceName = "GroupChatService";
            server = ( IChatServer )Naming.lookup("rmi://" + hostName + "/" + serviceName);
        }
        catch (ConnectException e) {
            JOptionPane.showMessageDialog(
                    chatGUI.frame, " servidor parece estar indisponível\nPor favor tente novamente mais tarde",
                    "Problema de conexão", JOptionPane.ERROR_MESSAGE);
            isConnected = false;
        }
        catch(NotBoundException | MalformedURLException me){
            isConnected = false;
        }
        if(!isConnected){
            registerWithServer(details);
        }
    }

    public void registerWithServer(String[] details) throws RemoteException {
            server.passIDentity(this.ref);
            server.registerListener(details);
    }

    @Override
    public void messageFromServer(String message) throws RemoteException {
        System.out.println( message );
        chatGUI.textArea.append( message );
        chatGUI.textArea.setCaretPosition(chatGUI.textArea.getDocument().getLength());
    }

    @Override
    public void updateUserList(String[] currentUsers) throws RemoteException {
        if(currentUsers.length < 2){
            chatGUI.privateMsgButton.setEnabled(false);
        }
        chatGUI.userPanel.remove(chatGUI.clientPanel);
        chatGUI.setClientPanel(currentUsers);
        chatGUI.clientPanel.repaint();
        chatGUI.clientPanel.revalidate();
    }
}
