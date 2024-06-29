package com.edu.utfpr.server;

import com.edu.utfpr.client.IChatClient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Usuario {
    public String name;
    public IChatClient client;
}
