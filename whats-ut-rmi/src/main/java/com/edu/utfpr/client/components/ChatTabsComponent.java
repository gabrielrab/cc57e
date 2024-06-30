package com.edu.utfpr.client.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.edu.utfpr.client.ChatClient;
import com.edu.utfpr.core.entities.Chat;
import com.edu.utfpr.core.entities.User;

public class ChatTabsComponent extends JPanel {
    private JTabbedPane tabbedPane;
    private JPanel chatsPanel, usersPanel, groupsPanel;

    private JList<Chat> myChatsList, groupsList;
    private DefaultListModel<Chat> myChatsModel, groupsModel;

    private JList<User> allUsersList;
    private DefaultListModel<User> allUsersModel;

    private ChatClient chatClient;

    public ChatTabsComponent(ChatClient chatClient) throws RemoteException {
        this.chatClient = chatClient;

        List<User> currentOnlineUsers = new ArrayList<>(chatClient.getCurrentUsers());
        currentOnlineUsers.removeIf(user -> user.getName().equals(chatClient.userName));
        List<Chat> groups = chatClient.getAllGroups();
        List<Chat> myChats = chatClient.getMyChats();

        initComponents(currentOnlineUsers, groups, myChats);
    }

    private void initComponents(List<User> currentOnlineUsers, List<Chat> groups, List<Chat> myChats) {
        tabbedPane = new JTabbedPane();

        setupMyChatsTab(myChats);
        setupUsersTab(currentOnlineUsers);
        setupGroupsTab(groups);

        add(tabbedPane);

        setupListeners();
        updateLists(currentOnlineUsers, groups, myChats);
    }

    private void setupMyChatsTab(List<Chat> myChats) {
        myChatsModel = new DefaultListModel<>();
        myChatsList = new JList<>(myChatsModel);
        myChatsList.setCellRenderer(createChatListsCellRenderer());
        myChatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Chat chat = myChatsList.getSelectedValue();
                    try {
                        chatClient.setCurrentChat(chat);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    tabbedPane.setSelectedIndex(0);
                }
            }
        });
        chatsPanel = new JPanel(new BorderLayout());
        chatsPanel.add(new JScrollPane(myChatsList), BorderLayout.CENTER);
        tabbedPane.addTab("Meus Chats", chatsPanel);
    }

    private void setupUsersTab(List<User> currentOnlineUsers) {
        allUsersModel = new DefaultListModel<>();
        allUsersList = new JList<>(allUsersModel);
        allUsersList.setCellRenderer(createUsersListsCellRenderer());
        allUsersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    User user = allUsersList.getSelectedValue();
                    try {
                        chatClient.createPrivateChat(user.getName());
                        tabbedPane.setSelectedIndex(0);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(new JScrollPane(allUsersList), BorderLayout.CENTER);
        tabbedPane.addTab("Usuários online", usersPanel);
    }

    private void setupGroupsTab(List<Chat> groups) {
        groupsModel = new DefaultListModel<>();
        groupsList = new JList<>(groupsModel);
        groupsList.setCellRenderer(createChatListsCellRenderer());
        groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.add(new JScrollPane(groupsList), BorderLayout.CENTER);
        tabbedPane.addTab("Grupos públicos", groupsPanel);
    }

    private ListCellRenderer<? super User> createUsersListsCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((User) value).getName());
                return this;
            }
        };
    }

    private ListCellRenderer<? super Chat> createChatListsCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((Chat) value).getName());
                return this;
            }
        };
    }

    private void setupListeners() {
        chatClient.addChangeUserListListener(this::onChangeUserList);
        chatClient.addChangeGroupListListener(this::onChangeGroupList);
        chatClient.addChangeMyChatsListListener(this::onChangeMyChatsList);
    }

    private void updateLists(List<User> currentOnlineUsers, List<Chat> groups, List<Chat> myChats) {
        onChangeUserList(currentOnlineUsers);
        onChangeGroupList(groups);
        onChangeMyChatsList(myChats);
    }

    private void onChangeUserList(List<User> onlineUsers) {
        allUsersModel.clear();
        onlineUsers.forEach(allUsersModel::addElement);
    }

    private void onChangeGroupList(List<Chat> groups) {
        groupsModel.clear();
        groups.forEach(groupsModel::addElement);
    }

    private void onChangeMyChatsList(List<Chat> myChats) {
        myChatsModel.clear();
        myChats.forEach(myChatsModel::addElement);
    }
}
