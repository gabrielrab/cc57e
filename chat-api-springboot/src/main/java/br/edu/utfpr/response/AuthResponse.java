package br.edu.utfpr.response;

import lombok.Data;

@Data
public class AuthResponse {
	private String jwt;
	private boolean status;
}
