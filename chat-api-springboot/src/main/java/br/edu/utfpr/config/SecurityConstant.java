package br.edu.utfpr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration("securityConstantBean")
public class SecurityConstant {
	private static String JWT_KEY_STATIC;
	public static String HEADER = "Authorization";

	@Value("${jwt.secret}")
	private String jwtKey;

	@PostConstruct
	public void init() {
		JWT_KEY_STATIC = jwtKey;
	}

	public static String getJwtKeyStatic() {
		return JWT_KEY_STATIC;
	}
}
