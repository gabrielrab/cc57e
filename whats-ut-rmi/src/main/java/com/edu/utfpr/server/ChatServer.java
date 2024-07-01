package com.edu.utfpr.server;

import com.edu.utfpr.client.IChatClient;
import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.Messages;
import com.edu.utfpr.core.entities.User;
import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ChatServer extends UnicastRemoteObject implements IChatServer {
    private final Vector<User> users;
    private final Map<String, String> userCredentials = new HashMap<>();
    private final List<Chat> groups = new ArrayList<>();
    private final List<Chat> privateGroups = new ArrayList<>();

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
            ChatServer server = new ChatServer();
            Naming.rebind("rmi://" + hostName + "/" + serviceName, server);
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

    @Override
    public void createChatGroup(String chatName, String creatorUsername, boolean exitAdminMethodRandom)
            throws RemoteException {
        User creator = users.stream().filter(user -> user.getName().equals(creatorUsername)).findFirst().orElse(null);

        UUID guid = UUID.randomUUID();

        Chat chat = new Chat(
                guid,
                chatName,
                creator,
                creator,
                true,
                exitAdminMethodRandom);
        groups.add(chat);
        updatePublicGroupList();
        updateMyChatsList(creator);
    }


    @Override
    public void createInviteGroup(String userName, Chat chat) throws RemoteException {
        User userParam = users.stream().filter(user -> user.getName().equals(userName)).findFirst().orElse(null);
        Boolean hasInvite = chat.pendingUsers.contains(userParam);
        Boolean isIn = chat.members.contains(userParam);
        if (!hasInvite && !isIn) {
            chat.pendingUsers.add(userParam);
        }
        System.err.println("Lista de pedidos" + chat.pendingUsers);
    }

    @Override
    public void acceptInviteGroup(String userName, Chat chat) throws RemoteException {

        User userParam = users.stream().filter(user -> user.getName().equals(userName)).findFirst().orElse(null);
        int indexUser = chat.pendingUsers.indexOf(userParam);

        chat.pendingUsers.remove(indexUser);
        chat.members.add(userParam);

        updatePublicGroupList();
        updateMyChatsList(userParam);
        
    }

    @Override
    public void createPrivateChat(String user1, String user2) throws RemoteException {
        UUID guid = UUID.randomUUID();

        User currentUser = users.stream().filter(user -> user.getName().equals(user1)).findFirst().orElse(null);

        User destinationUser = users.stream().filter(user -> user.getName().equals(user2)).findFirst().orElse(null);

        Chat chat = new Chat(
                guid,
                currentUser.getName() + " e " + destinationUser.getName(),
                currentUser,
                currentUser,
                false,
                false,
                List.of(currentUser, destinationUser),
                new ArrayList<>(),
                new ArrayList<>());

        privateGroups.add(chat);

        updateMyChatsList(currentUser);
        updateMyChatsList(destinationUser);
    }

    @Override
    public void leaveGroup(String UserName, Chat chat) throws RemoteException {

        User currentUser = users.stream().filter(user -> user.getName().equals(UserName)).findFirst().orElse(null);

        boolean isAdmin = false;
        if (chat.admin == currentUser) {
            isAdmin = true;
        }

        if (!isAdmin) {
            int indexUser = chat.members.indexOf(currentUser);
            chat.members.remove(indexUser);
            updatePublicGroupList();
        } else {
            if (chat.exitAdminMethodRandom) {
                if (chat.members.size() > 1) {
                    int indexUser = chat.members.indexOf(currentUser);
                    chat.members.remove(indexUser);
                    chat.admin = chat.members.get(0);
                    updatePublicGroupList();
                } else {
                    groups.remove(chat);
                    updatePublicGroupList();
                }
            } else {
                groups.remove(chat);
                updatePublicGroupList();
            }
        }
    }

    @Override
    public void login(String userName, String password, String hostName, String clientServiceName)
            throws RemoteException, MalformedURLException, NotBoundException, InvalidUserOrPasswordException {
        if (userCredentials.containsKey(userName) && userCredentials.get(userName).equals(password)) {
            IChatClient nextClient = (IChatClient) Naming.lookup("rmi://" + hostName + "/" + clientServiceName);
            users.addElement(new User(userName, nextClient));
            updateUserList();
        } else {
            throw new InvalidUserOrPasswordException();
        }
    }

    @Override
    public void registerUser(String userName, String password)
            throws RemoteException, UserAlreadyRegisteredException {
        if (userCredentials.containsKey(userName)) {
            throw new UserAlreadyRegisteredException();
        }

        userCredentials.put(userName, password);
    }

    private void updateUserList() {
        List<User> currentUsers = getUserList();
        currentUsers.removeIf(user -> user.getClient() == null);
        for (User c : users) {
            try {
                if (c.getClient() != null) {
                    List<User> candidates = new ArrayList<>(currentUsers);
                    candidates.remove(c);
                    c.getClient().updateUserList(candidates);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePublicGroupList() {
        List<User> currentUsers = getUserList();
        currentUsers.removeIf(user -> user.getClient() == null);
        for (User c : users) {
            try {
                if (c.getClient() != null) {
                    c.getClient().updatePublicGroupList(groups);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMyChatsList(User currentUser) {
        List<Chat> myChats;
        for (User user : users) {
            try {
                myChats = getMyChats(currentUser.getName());
                user.getClient().updateChatList(myChats);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private List<User> getUserList() {
        List<User> allUsers = new ArrayList<>(users.size());
        allUsers.addAll(users);
        return allUsers;
    }

    @Override
    public List<User> getCurrentUsers() throws RemoteException {
        return getUserList();
    }

    @Override
    public List<Chat> getAllGroups() throws RemoteException {
        return groups;
    }

    @Override
    public List<Chat> getMyChats(String userName) throws RemoteException {
        List<Chat> myChats = new ArrayList<>();

        for (Chat chat : groups) {
            boolean userFound = chat.members.stream().anyMatch(member -> member.name.equals(userName));
            if (userFound) {
                myChats.add(chat);
            }
        }

        for (Chat chat : privateGroups) {
            boolean userFound = chat.members.stream().anyMatch(member -> member.name.equals(userName));
            if (userFound) {
                myChats.add(chat);
            }
        }

        return myChats;
    }

    @Override
    public void sendMessage(String user, Chat chat, String message) throws RemoteException {
        User userFound = users.stream().filter(u -> u.name.equals(user)).findFirst().orElse(null);
        Messages newMessage;

        if (chat.isGroup) {
            switch (message) {
                case "/members":
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Lista de membros do grupo "+ chat.name);
                    for (User u : chat.members) {
                        JLabel uName = new JLabel("User "+ u.name);
                        dialog.add(uName);
                    }
                    dialog.setVisible(true);
                    dialog.setLayout(new BorderLayout());
                    dialog.setAlwaysOnTop(true);
                    newMessage = null;
                break;
            
                default:
                    Chat group = groups.get(groups.indexOf(chat));
                    newMessage = new Messages(userFound, message);
                    group.messages.add(newMessage);
                break;
            }
            
        } else {
            Chat privateGroup = privateGroups.stream()
                    .filter(other -> chat.getChatId().equals(other.getChatId()))
                    .findFirst()
                    .orElse(null);
            newMessage = new Messages(userFound, message);

            if (privateGroup != null) {
                privateGroup.messages.add(newMessage);
            }
        }

        for (User groupUser : chat.members) {
            if (groupUser.getClient() != null) {
                if (groupUser.getClient().getCurrentChatId().equals(chat.chatId ) && newMessage != null) {
                    groupUser.getClient().receiveMessage(newMessage);
                }
            }
        }
    }

}
