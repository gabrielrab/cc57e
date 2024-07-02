package com.edu.utfpr.client.components;

import com.edu.utfpr.client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class NewGroupDialog {

    public void openCreateGroupDialog(ChatClient chatClient) {

        JDialog dialog = new JDialog((Frame) null, "Criar Novo Grupo", true);
        dialog.setSize(500, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setAlwaysOnTop(true);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Nome do Grupo:");
        JTextField nameField = new JTextField();

        JRadioButton radio1 = new JRadioButton("Quando admin sair escolher outra pessoa para admin");

        JRadioButton radio2 = new JRadioButton("Quando admin sair excluir grupo");
        radio2.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(radio1);

        formPanel.add(radio2);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Criar");
        JButton cancelButton = new JButton("Cancelar");

        createButton.addActionListener(e -> {
            String groupName = nameField.getText();
            Boolean radioRamdom = radio1.isSelected();
            if (!groupName.trim().isEmpty()) {
                try {
                    chatClient.createChatGroup(groupName, chatClient.userName, radioRamdom);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Erro ao criar grupo!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    dialog.dispose();
                }
                System.out.println("Grupo " + groupName + " criado!");
                JOptionPane.showMessageDialog(dialog, "Grupo " + groupName + " criado!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "O nome do grupo não pode estar vazio.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
