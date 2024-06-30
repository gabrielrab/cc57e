package com.edu.utfpr.client.components;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.edu.utfpr.client.ChatClient;

public class ChatRenderComponent extends JPanel {
    private JTextArea textArea;
    private JPanel textPanel;

    public ChatRenderComponent(ChatClient chatClient) throws RemoteException {
        initComponent();

        chatClient.addChangeCurrentChatListener(chat -> {
            textArea.setText("");
            if (chat != null) {
                textArea.append("Chat: " + chat.getName() + "\n");
                textArea.append("Mensagens:\n");
                chat.getMessages().forEach(message -> {
                    textArea.append(message.getSender().getName() + ": " + message.getContent() + "\n");
                });
            }
        });

        chatClient.addOnReceiveMessageListener(message -> {
            textArea.append(message + "\n");
        });
    }

    private void initComponent() {
        String welcome = "Bem-vindo ao WhatsUT, selecione um chat e comece uma conversa...\n";
        textArea = new JTextArea(welcome, 14, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel = new JPanel(new BorderLayout());
        textPanel.add(scrollPane, BorderLayout.CENTER);

        add(textPanel);
    }

}