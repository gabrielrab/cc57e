package com.edu.utfpr.core.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Messages implements Serializable {
    public Chat chat;
    public User sender;
    public String content;
}
