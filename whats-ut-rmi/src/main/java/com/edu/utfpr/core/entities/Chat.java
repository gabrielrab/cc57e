package com.edu.utfpr.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat implements Serializable {
    public UUID chatId;
    public String name;

    public User admin;
    public User created_by;

    public Boolean isGroup;
    public Boolean exitAdminMethodRandom;

    public List<User> members = new ArrayList<>();

    public List<User> pendingUsers = new ArrayList<>();

    public List<Messages> messages = new ArrayList<>();

    public Chat(UUID chatId, String name, User admin, User created_by, Boolean isGroup, Boolean exitAdminMethodRandom) {
        this.chatId = chatId;
        this.name = name;
        this.admin = admin;
        this.created_by = created_by;
        this.isGroup = isGroup;
        this.exitAdminMethodRandom = exitAdminMethodRandom;

        this.members.add(admin);
    }
}
