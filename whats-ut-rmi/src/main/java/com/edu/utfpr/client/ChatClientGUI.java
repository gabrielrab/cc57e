package com.edu.utfpr.client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.rmi.RemoteException;

public class ChatClientGUI extends JFrame implements ActionListener {
    private final Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
    private final Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);//top,r,b,l
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton privateMsgButton, startButton, sendButton;
    protected JPanel clientPanel, userPanel;
    private JPanel textPanel, inputPanel;
    private JTextField textField;
    private String name, message;
    private ChatClient chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;

    public ChatClientGUI(ChatClient chatClient) {
        frame = new JFrame("WhatsUI");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (chatClient != null) {
                    try {
                        chatClient.server.leaveChat(name);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.exit(0);
            }
        });

        Container c = getContentPane();
        JPanel outerPanel = new JPanel(new BorderLayout());

        outerPanel.add(getInputPanel(), BorderLayout.CENTER);
        outerPanel.add(getTextPanel(), BorderLayout.NORTH);

        c.setLayout(new BorderLayout());
        c.add(outerPanel, BorderLayout.CENTER);
        c.add(getUsersPanel(), BorderLayout.WEST);

        frame.add(c);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocation(150, 150);
        textField.requestFocus();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

        chatClient.addChangeUserListListener(this::onChangeUserList);
        chatClient.addOnReceiveMessageListener(this::onReceiveMessage);
    }

    public JPanel getTextPanel() {
        String welcome = "Bem-vindo, digite seu nome e pressione enter\n";
        textArea = new JTextArea(welcome, 14, 34);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(meiryoFont);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel = new JPanel();
        textPanel.add(scrollPane);

        textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
        return textPanel;
    }

    public JPanel getInputPanel() {
        inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        inputPanel.setBorder(blankBorder);
        textField = new JTextField();
        textField.setFont(meiryoFont);
        inputPanel.add(textField);
        return inputPanel;
    }

    public JPanel getUsersPanel() {

        userPanel = new JPanel(new BorderLayout());
        String userStr = "Usuários online";

        JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
        userPanel.add(userLabel, BorderLayout.NORTH);
        userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

        String[] noClientsYet = {"Não há outros usuários no momento"};
        setClientPanel(noClientsYet);

        clientPanel.setFont(meiryoFont);
        userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
        userPanel.setBorder(blankBorder);

        return userPanel;
    }

    public void setClientPanel(String[] currClients) {
        clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();

        for (String s : currClients) {
            listModel.addElement(s);
        }
        if (currClients.length > 1) {
            privateMsgButton.setEnabled(true);
        }

        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(meiryoFont);
        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }

    public JPanel makeButtonPanel() {
        sendButton = new JButton("Enviar");
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);

        privateMsgButton = new JButton("Enviar PV");
        privateMsgButton.addActionListener(this);
        privateMsgButton.setEnabled(false);

        startButton = new JButton("Começar");
        startButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(privateMsgButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(startButton);
        buttonPanel.add(sendButton);

        return buttonPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == sendButton) {
                message = textField.getText();
                textField.setText("");
                sendMessage(message);
                System.out.println("Enviando mensagem : " + message);
            }

            if (e.getSource() == privateMsgButton) {
                int[] privateList = list.getSelectedIndices();

                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("Indíce selecionado :" + privateList[i]);
                }
                message = textField.getText();
                textField.setText("");
                sendPrivate(privateList);
            }

        } catch (RemoteException remoteExc) {
            remoteExc.printStackTrace();
        }

    }

    private void sendMessage(String chatMessage) throws RemoteException {
        chatClient.server.updateChat(name, chatMessage);
    }

    private void sendPrivate(int[] privateList) throws RemoteException {
        String privateMessage = "[Mensagem privada de " + name + "] :" + message + "\n";
        chatClient.server.sendPM(privateList, privateMessage);
    }

    private void onChangeUserList(String[] currentUsers) {
        if (currentUsers.length < 2) {
            privateMsgButton.setEnabled(false);
        }
        userPanel.remove(clientPanel);
        setClientPanel(currentUsers);
        clientPanel.repaint();
        clientPanel.revalidate();
    }

    private void onReceiveMessage(String message) {
        textArea.append(message);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
