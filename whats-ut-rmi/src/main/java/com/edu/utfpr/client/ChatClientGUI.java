package com.edu.utfpr.client;

import javax.swing.*;

import com.edu.utfpr.client.components.ChatRenderComponent;
import com.edu.utfpr.client.components.ChatTabsComponent;
import com.edu.utfpr.client.components.NewGroupDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.rmi.RemoteException;

public class ChatClientGUI extends JFrame {
    private ChatTabsComponent chatTabsComponent;
    private ChatRenderComponent chatRenderComponent;
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton privateMsgButton, startButton, sendButton;
    protected JPanel clientPanel, userPanel;
    private JPanel inputPanel;
    private JTextField textField;
    private ChatClient chatClient;

    public ChatClientGUI(ChatClient chatClient) throws RemoteException {
        this.chatClient = chatClient;

        frame = new JFrame("WhatsUI - " + chatClient.userName);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (chatClient != null) {
                    // TODO: remover usuário da listagem de usuários online
                }
                System.exit(0);
            }
        });

        Container c = getContentPane();
        JPanel outerPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = createChatPanel();

        outerPanel.add(createChatMessagesPanel(), BorderLayout.NORTH);
        outerPanel.add(createSendMessageInput(), BorderLayout.CENTER);

        c.setLayout(new BorderLayout());
        c.add(outerPanel, BorderLayout.CENTER);
        c.add(leftPanel, BorderLayout.WEST);
        leftPanel.add(createGroupButton(), BorderLayout.NORTH);

        frame.add(c);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocation(150, 150);
        textField.requestFocus();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel createChatMessagesPanel() throws RemoteException {
        chatRenderComponent = new ChatRenderComponent(chatClient);
        return chatRenderComponent;
    }

    public JPanel createChatPanel() throws RemoteException {
        chatTabsComponent = new ChatTabsComponent(chatClient);

        return chatTabsComponent;
    }

    public JButton createGroupButton() throws RemoteException {
        sendButton = new JButton("+ Criar Novo Grupo");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                NewGroupDialog DialogCreateGroup = new NewGroupDialog();
                DialogCreateGroup.openCreateGroupDialog(chatClient);
            }
        });
        sendButton.setEnabled(true);

        return sendButton;
    }

    public JPanel createSendMessageInput() {
        inputPanel = new JPanel(new BorderLayout());
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                if (!message.trim().isEmpty()) {
                    try {
                        chatClient.sendMessage(message);
                        textField.setText("");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        inputPanel.add(textField);
        return inputPanel;
    }
}
