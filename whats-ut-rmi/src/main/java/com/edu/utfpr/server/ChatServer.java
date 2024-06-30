package com.edu.utfpr.server;

import com.edu.utfpr.client.ChatClient;
import com.edu.utfpr.client.IChatClient;
import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.Messages;
import com.edu.utfpr.core.entities.User;
import com.edu.utfpr.core.exceptions.InvalidUserOrPasswordException;
import com.edu.utfpr.core.exceptions.UserAlreadyRegisteredException;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.UUID;

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
                exitAdminMethodRandom,
                true);
        groups.add(chat);
        updatePublicGroupList();
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
    public void registerUser(String userName, String password, String hostName, String clientServiceName)
            throws RemoteException, UserAlreadyRegisteredException, MalformedURLException, NotBoundException {
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
        List<Chat> myChats = new ArrayList<>();
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
        for (User user : users) {
            allUsers.add(user);
        }
        return allUsers;
    }

    @Override
    public void leaveGroup(User user, Chat chat) throws RemoteException {
        boolean isAdmin = false;
        if (chat.admin == user) {
            isAdmin = true;
        }

        if (!isAdmin) {
            int indexUser = chat.members.indexOf(user);
            chat.members.remove(indexUser);
            updatePublicGroupList();
        } else {
            if (chat.exitAdminMethodRandom) {
                if (chat.members.size() > 1) {
                    int indexUser = chat.members.indexOf(user);
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

        for (User c : users) {
            if (c.getName().equals(user.name)) {
                System.out.println(user.name + " saiu do grupo");
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
    public void createInviteGroup(User user, Chat chat) throws RemoteException {
        chat.pendingUsers.add(user);
    }

    @Override
    public void acceptInviteGroup(User user, Chat chat) throws RemoteException {
        int indexUser = chat.pendingUsers.indexOf(user);

        chat.pendingUsers.remove(indexUser);
        chat.members.add(user);
    }

    @Override
    public List<User> getPendingUsersGroup(Chat chat) throws RemoteException {
        return chat.pendingUsers;
    }

    @Override
    public List<User> getMembersGroup(Chat chat) throws RemoteException {
        return chat.members;
    }

    @Override
    public void sendMessage(String user, Chat chat, String message) throws RemoteException {
        User userFound = users.stream().filter(u -> u.name.equals(user)).findFirst().orElse(null);
        Messages newMessage;

        if (chat.isGroup) {
            Chat group = groups.get(groups.indexOf(chat));
            newMessage = new Messages(group, userFound, message);
            group.messages.add(newMessage);
        } else {
            Chat privateGroup = privateGroups.stream()
                    .filter(other -> chat.getChatId().equals(other.getChatId()))
                    .findFirst()
                    .orElse(null);
            newMessage = new Messages(privateGroup, userFound, message);
            privateGroup.messages.add(newMessage);
        }

        for (User groupUser : chat.members) {
            if (groupUser.getClient() != null) {
                if (groupUser.getClient().getCurrentChatId().equals(chat.chatId)) {
                    groupUser.getClient().receiveMessage(newMessage);
                }
            }
        }
    }

}
