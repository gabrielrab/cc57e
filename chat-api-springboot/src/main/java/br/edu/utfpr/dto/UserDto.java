package br.edu.utfpr.dto;

import lombok.Data;

@Data
public class UserDto {
	private Integer id;
	private String full_name;
	private String email;
	private String profile_picture;
}
